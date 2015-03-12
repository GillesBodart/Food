

package bodart.food.db.controller;

import bodart.food.db.controller.exceptions.IllegalOrphanException;
import bodart.food.db.controller.exceptions.NonexistentEntityException;
import bodart.food.db.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Unit;
import bodart.food.db.entity.Aliment;
import java.util.ArrayList;
import java.util.List;
import bodart.food.db.entity.Ingredient;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class UnitJpaController implements Serializable {

    public UnitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Unit unit) throws PreexistingEntityException, Exception {
        if (unit.getAlimentList() == null) {
            unit.setAlimentList(new ArrayList<Aliment>());
        }
        if (unit.getUnitList() == null) {
            unit.setUnitList(new ArrayList<Unit>());
        }
        if (unit.getIngredientList() == null) {
            unit.setIngredientList(new ArrayList<Ingredient>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unit unitparent = unit.getUnitparent();
            if (unitparent != null) {
                unitparent = em.getReference(unitparent.getClass(), unitparent.getUnitid());
                unit.setUnitparent(unitparent);
            }
            List<Aliment> attachedAlimentList = new ArrayList<Aliment>();
            for (Aliment alimentListAlimentToAttach : unit.getAlimentList()) {
                alimentListAlimentToAttach = em.getReference(alimentListAlimentToAttach.getClass(), alimentListAlimentToAttach.getAliid());
                attachedAlimentList.add(alimentListAlimentToAttach);
            }
            unit.setAlimentList(attachedAlimentList);
            List<Unit> attachedUnitList = new ArrayList<Unit>();
            for (Unit unitListUnitToAttach : unit.getUnitList()) {
                unitListUnitToAttach = em.getReference(unitListUnitToAttach.getClass(), unitListUnitToAttach.getUnitid());
                attachedUnitList.add(unitListUnitToAttach);
            }
            unit.setUnitList(attachedUnitList);
            List<Ingredient> attachedIngredientList = new ArrayList<Ingredient>();
            for (Ingredient ingredientListIngredientToAttach : unit.getIngredientList()) {
                ingredientListIngredientToAttach = em.getReference(ingredientListIngredientToAttach.getClass(), ingredientListIngredientToAttach.getIngid());
                attachedIngredientList.add(ingredientListIngredientToAttach);
            }
            unit.setIngredientList(attachedIngredientList);
            em.persist(unit);
            if (unitparent != null) {
                unitparent.getUnitList().add(unit);
                unitparent = em.merge(unitparent);
            }
            for (Aliment alimentListAliment : unit.getAlimentList()) {
                Unit oldAliunitOfAlimentListAliment = alimentListAliment.getAliunit();
                alimentListAliment.setAliunit(unit);
                alimentListAliment = em.merge(alimentListAliment);
                if (oldAliunitOfAlimentListAliment != null) {
                    oldAliunitOfAlimentListAliment.getAlimentList().remove(alimentListAliment);
                    oldAliunitOfAlimentListAliment = em.merge(oldAliunitOfAlimentListAliment);
                }
            }
            for (Unit unitListUnit : unit.getUnitList()) {
                Unit oldUnitparentOfUnitListUnit = unitListUnit.getUnitparent();
                unitListUnit.setUnitparent(unit);
                unitListUnit = em.merge(unitListUnit);
                if (oldUnitparentOfUnitListUnit != null) {
                    oldUnitparentOfUnitListUnit.getUnitList().remove(unitListUnit);
                    oldUnitparentOfUnitListUnit = em.merge(oldUnitparentOfUnitListUnit);
                }
            }
            for (Ingredient ingredientListIngredient : unit.getIngredientList()) {
                Unit oldIngunitOfIngredientListIngredient = ingredientListIngredient.getIngunit();
                ingredientListIngredient.setIngunit(unit);
                ingredientListIngredient = em.merge(ingredientListIngredient);
                if (oldIngunitOfIngredientListIngredient != null) {
                    oldIngunitOfIngredientListIngredient.getIngredientList().remove(ingredientListIngredient);
                    oldIngunitOfIngredientListIngredient = em.merge(oldIngunitOfIngredientListIngredient);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUnit(unit.getUnitid()) != null) {
                throw new PreexistingEntityException("Unit " + unit + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Unit unit) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unit persistentUnit = em.find(Unit.class, unit.getUnitid());
            Unit unitparentOld = persistentUnit.getUnitparent();
            Unit unitparentNew = unit.getUnitparent();
            List<Aliment> alimentListOld = persistentUnit.getAlimentList();
            List<Aliment> alimentListNew = unit.getAlimentList();
            List<Unit> unitListOld = persistentUnit.getUnitList();
            List<Unit> unitListNew = unit.getUnitList();
            List<Ingredient> ingredientListOld = persistentUnit.getIngredientList();
            List<Ingredient> ingredientListNew = unit.getIngredientList();
            List<String> illegalOrphanMessages = null;
            for (Aliment alimentListOldAliment : alimentListOld) {
                if (!alimentListNew.contains(alimentListOldAliment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Aliment " + alimentListOldAliment + " since its aliunit field is not nullable.");
                }
            }
            for (Ingredient ingredientListOldIngredient : ingredientListOld) {
                if (!ingredientListNew.contains(ingredientListOldIngredient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ingredient " + ingredientListOldIngredient + " since its ingunit field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (unitparentNew != null) {
                unitparentNew = em.getReference(unitparentNew.getClass(), unitparentNew.getUnitid());
                unit.setUnitparent(unitparentNew);
            }
            List<Aliment> attachedAlimentListNew = new ArrayList<Aliment>();
            for (Aliment alimentListNewAlimentToAttach : alimentListNew) {
                alimentListNewAlimentToAttach = em.getReference(alimentListNewAlimentToAttach.getClass(), alimentListNewAlimentToAttach.getAliid());
                attachedAlimentListNew.add(alimentListNewAlimentToAttach);
            }
            alimentListNew = attachedAlimentListNew;
            unit.setAlimentList(alimentListNew);
            List<Unit> attachedUnitListNew = new ArrayList<Unit>();
            for (Unit unitListNewUnitToAttach : unitListNew) {
                unitListNewUnitToAttach = em.getReference(unitListNewUnitToAttach.getClass(), unitListNewUnitToAttach.getUnitid());
                attachedUnitListNew.add(unitListNewUnitToAttach);
            }
            unitListNew = attachedUnitListNew;
            unit.setUnitList(unitListNew);
            List<Ingredient> attachedIngredientListNew = new ArrayList<Ingredient>();
            for (Ingredient ingredientListNewIngredientToAttach : ingredientListNew) {
                ingredientListNewIngredientToAttach = em.getReference(ingredientListNewIngredientToAttach.getClass(), ingredientListNewIngredientToAttach.getIngid());
                attachedIngredientListNew.add(ingredientListNewIngredientToAttach);
            }
            ingredientListNew = attachedIngredientListNew;
            unit.setIngredientList(ingredientListNew);
            unit = em.merge(unit);
            if (unitparentOld != null && !unitparentOld.equals(unitparentNew)) {
                unitparentOld.getUnitList().remove(unit);
                unitparentOld = em.merge(unitparentOld);
            }
            if (unitparentNew != null && !unitparentNew.equals(unitparentOld)) {
                unitparentNew.getUnitList().add(unit);
                unitparentNew = em.merge(unitparentNew);
            }
            for (Aliment alimentListNewAliment : alimentListNew) {
                if (!alimentListOld.contains(alimentListNewAliment)) {
                    Unit oldAliunitOfAlimentListNewAliment = alimentListNewAliment.getAliunit();
                    alimentListNewAliment.setAliunit(unit);
                    alimentListNewAliment = em.merge(alimentListNewAliment);
                    if (oldAliunitOfAlimentListNewAliment != null && !oldAliunitOfAlimentListNewAliment.equals(unit)) {
                        oldAliunitOfAlimentListNewAliment.getAlimentList().remove(alimentListNewAliment);
                        oldAliunitOfAlimentListNewAliment = em.merge(oldAliunitOfAlimentListNewAliment);
                    }
                }
            }
            for (Unit unitListOldUnit : unitListOld) {
                if (!unitListNew.contains(unitListOldUnit)) {
                    unitListOldUnit.setUnitparent(null);
                    unitListOldUnit = em.merge(unitListOldUnit);
                }
            }
            for (Unit unitListNewUnit : unitListNew) {
                if (!unitListOld.contains(unitListNewUnit)) {
                    Unit oldUnitparentOfUnitListNewUnit = unitListNewUnit.getUnitparent();
                    unitListNewUnit.setUnitparent(unit);
                    unitListNewUnit = em.merge(unitListNewUnit);
                    if (oldUnitparentOfUnitListNewUnit != null && !oldUnitparentOfUnitListNewUnit.equals(unit)) {
                        oldUnitparentOfUnitListNewUnit.getUnitList().remove(unitListNewUnit);
                        oldUnitparentOfUnitListNewUnit = em.merge(oldUnitparentOfUnitListNewUnit);
                    }
                }
            }
            for (Ingredient ingredientListNewIngredient : ingredientListNew) {
                if (!ingredientListOld.contains(ingredientListNewIngredient)) {
                    Unit oldIngunitOfIngredientListNewIngredient = ingredientListNewIngredient.getIngunit();
                    ingredientListNewIngredient.setIngunit(unit);
                    ingredientListNewIngredient = em.merge(ingredientListNewIngredient);
                    if (oldIngunitOfIngredientListNewIngredient != null && !oldIngunitOfIngredientListNewIngredient.equals(unit)) {
                        oldIngunitOfIngredientListNewIngredient.getIngredientList().remove(ingredientListNewIngredient);
                        oldIngunitOfIngredientListNewIngredient = em.merge(oldIngunitOfIngredientListNewIngredient);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = unit.getUnitid();
                if (findUnit(id) == null) {
                    throw new NonexistentEntityException("The unit with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unit unit;
            try {
                unit = em.getReference(Unit.class, id);
                unit.getUnitid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The unit with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Aliment> alimentListOrphanCheck = unit.getAlimentList();
            for (Aliment alimentListOrphanCheckAliment : alimentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Unit (" + unit + ") cannot be destroyed since the Aliment " + alimentListOrphanCheckAliment + " in its alimentList field has a non-nullable aliunit field.");
            }
            List<Ingredient> ingredientListOrphanCheck = unit.getIngredientList();
            for (Ingredient ingredientListOrphanCheckIngredient : ingredientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Unit (" + unit + ") cannot be destroyed since the Ingredient " + ingredientListOrphanCheckIngredient + " in its ingredientList field has a non-nullable ingunit field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Unit unitparent = unit.getUnitparent();
            if (unitparent != null) {
                unitparent.getUnitList().remove(unit);
                unitparent = em.merge(unitparent);
            }
            List<Unit> unitList = unit.getUnitList();
            for (Unit unitListUnit : unitList) {
                unitListUnit.setUnitparent(null);
                unitListUnit = em.merge(unitListUnit);
            }
            em.remove(unit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Unit> findUnitEntities() {
        return findUnitEntities(true, -1, -1);
    }

    public List<Unit> findUnitEntities(int maxResults, int firstResult) {
        return findUnitEntities(false, maxResults, firstResult);
    }

    private List<Unit> findUnitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Unit.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Unit findUnit(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Unit.class, id);
        } finally {
            em.close();
        }
    }

    public int getUnitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Unit> rt = cq.from(Unit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

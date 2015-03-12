package bodart.food.db.controller;

import bodart.food.db.exceptions.IllegalOrphanException;
import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import bodart.food.db.entity.Aliment;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Provider;
import bodart.food.db.entity.Category;
import bodart.food.db.entity.Unit;
import bodart.food.db.entity.Ingredient;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class AlimentJpaController implements Serializable {

    public AlimentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aliment aliment) throws PreexistingEntityException, Exception {
        if (aliment.getIngredientList() == null) {
            aliment.setIngredientList(new ArrayList<Ingredient>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider aliprovider = aliment.getAliprovider();
            if (aliprovider != null) {
                aliprovider = em.getReference(aliprovider.getClass(), aliprovider.getProid());
                aliment.setAliprovider(aliprovider);
            }
            Category alicategory = aliment.getAlicategory();
            if (alicategory != null) {
                alicategory = em.getReference(alicategory.getClass(), alicategory.getCatid());
                aliment.setAlicategory(alicategory);
            }
            Unit aliunit = aliment.getAliunit();
            if (aliunit != null) {
                aliunit = em.getReference(aliunit.getClass(), aliunit.getUnitid());
                aliment.setAliunit(aliunit);
            }
            List<Ingredient> attachedIngredientList = new ArrayList<Ingredient>();
            for (Ingredient ingredientListIngredientToAttach : aliment.getIngredientList()) {
                ingredientListIngredientToAttach = em.getReference(ingredientListIngredientToAttach.getClass(), ingredientListIngredientToAttach.getIngid());
                attachedIngredientList.add(ingredientListIngredientToAttach);
            }
            aliment.setIngredientList(attachedIngredientList);
            em.persist(aliment);
            if (aliprovider != null) {
                aliprovider.getAlimentList().add(aliment);
                aliprovider = em.merge(aliprovider);
            }
            if (alicategory != null) {
                alicategory.getAlimentList().add(aliment);
                alicategory = em.merge(alicategory);
            }
            if (aliunit != null) {
                aliunit.getAlimentList().add(aliment);
                aliunit = em.merge(aliunit);
            }
            for (Ingredient ingredientListIngredient : aliment.getIngredientList()) {
                Aliment oldIngaliOfIngredientListIngredient = ingredientListIngredient.getIngali();
                ingredientListIngredient.setIngali(aliment);
                ingredientListIngredient = em.merge(ingredientListIngredient);
                if (oldIngaliOfIngredientListIngredient != null) {
                    oldIngaliOfIngredientListIngredient.getIngredientList().remove(ingredientListIngredient);
                    oldIngaliOfIngredientListIngredient = em.merge(oldIngaliOfIngredientListIngredient);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAliment(aliment.getAliid()) != null) {
                throw new PreexistingEntityException("Aliment " + aliment + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aliment aliment) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aliment persistentAliment = em.find(Aliment.class, aliment.getAliid());
            Provider aliproviderOld = persistentAliment.getAliprovider();
            Provider aliproviderNew = aliment.getAliprovider();
            Category alicategoryOld = persistentAliment.getAlicategory();
            Category alicategoryNew = aliment.getAlicategory();
            Unit aliunitOld = persistentAliment.getAliunit();
            Unit aliunitNew = aliment.getAliunit();
            List<Ingredient> ingredientListOld = persistentAliment.getIngredientList();
            List<Ingredient> ingredientListNew = aliment.getIngredientList();
            List<String> illegalOrphanMessages = null;
            for (Ingredient ingredientListOldIngredient : ingredientListOld) {
                if (!ingredientListNew.contains(ingredientListOldIngredient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ingredient " + ingredientListOldIngredient + " since its ingali field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (aliproviderNew != null) {
                aliproviderNew = em.getReference(aliproviderNew.getClass(), aliproviderNew.getProid());
                aliment.setAliprovider(aliproviderNew);
            }
            if (alicategoryNew != null) {
                alicategoryNew = em.getReference(alicategoryNew.getClass(), alicategoryNew.getCatid());
                aliment.setAlicategory(alicategoryNew);
            }
            if (aliunitNew != null) {
                aliunitNew = em.getReference(aliunitNew.getClass(), aliunitNew.getUnitid());
                aliment.setAliunit(aliunitNew);
            }
            List<Ingredient> attachedIngredientListNew = new ArrayList<Ingredient>();
            for (Ingredient ingredientListNewIngredientToAttach : ingredientListNew) {
                ingredientListNewIngredientToAttach = em.getReference(ingredientListNewIngredientToAttach.getClass(), ingredientListNewIngredientToAttach.getIngid());
                attachedIngredientListNew.add(ingredientListNewIngredientToAttach);
            }
            ingredientListNew = attachedIngredientListNew;
            aliment.setIngredientList(ingredientListNew);
            aliment = em.merge(aliment);
            if (aliproviderOld != null && !aliproviderOld.equals(aliproviderNew)) {
                aliproviderOld.getAlimentList().remove(aliment);
                aliproviderOld = em.merge(aliproviderOld);
            }
            if (aliproviderNew != null && !aliproviderNew.equals(aliproviderOld)) {
                aliproviderNew.getAlimentList().add(aliment);
                aliproviderNew = em.merge(aliproviderNew);
            }
            if (alicategoryOld != null && !alicategoryOld.equals(alicategoryNew)) {
                alicategoryOld.getAlimentList().remove(aliment);
                alicategoryOld = em.merge(alicategoryOld);
            }
            if (alicategoryNew != null && !alicategoryNew.equals(alicategoryOld)) {
                alicategoryNew.getAlimentList().add(aliment);
                alicategoryNew = em.merge(alicategoryNew);
            }
            if (aliunitOld != null && !aliunitOld.equals(aliunitNew)) {
                aliunitOld.getAlimentList().remove(aliment);
                aliunitOld = em.merge(aliunitOld);
            }
            if (aliunitNew != null && !aliunitNew.equals(aliunitOld)) {
                aliunitNew.getAlimentList().add(aliment);
                aliunitNew = em.merge(aliunitNew);
            }
            for (Ingredient ingredientListNewIngredient : ingredientListNew) {
                if (!ingredientListOld.contains(ingredientListNewIngredient)) {
                    Aliment oldIngaliOfIngredientListNewIngredient = ingredientListNewIngredient.getIngali();
                    ingredientListNewIngredient.setIngali(aliment);
                    ingredientListNewIngredient = em.merge(ingredientListNewIngredient);
                    if (oldIngaliOfIngredientListNewIngredient != null && !oldIngaliOfIngredientListNewIngredient.equals(aliment)) {
                        oldIngaliOfIngredientListNewIngredient.getIngredientList().remove(ingredientListNewIngredient);
                        oldIngaliOfIngredientListNewIngredient = em.merge(oldIngaliOfIngredientListNewIngredient);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = aliment.getAliid();
                if (findAliment(id) == null) {
                    throw new NonexistentEntityException("The aliment with id " + id + " no longer exists.");
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
            Aliment aliment;
            try {
                aliment = em.getReference(Aliment.class, id);
                aliment.getAliid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aliment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ingredient> ingredientListOrphanCheck = aliment.getIngredientList();
            for (Ingredient ingredientListOrphanCheckIngredient : ingredientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aliment (" + aliment + ") cannot be destroyed since the Ingredient " + ingredientListOrphanCheckIngredient + " in its ingredientList field has a non-nullable ingali field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Provider aliprovider = aliment.getAliprovider();
            if (aliprovider != null) {
                aliprovider.getAlimentList().remove(aliment);
                aliprovider = em.merge(aliprovider);
            }
            Category alicategory = aliment.getAlicategory();
            if (alicategory != null) {
                alicategory.getAlimentList().remove(aliment);
                alicategory = em.merge(alicategory);
            }
            Unit aliunit = aliment.getAliunit();
            if (aliunit != null) {
                aliunit.getAlimentList().remove(aliment);
                aliunit = em.merge(aliunit);
            }
            em.remove(aliment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aliment> findAlimentEntities() {
        return findAlimentEntities(true, -1, -1);
    }

    public List<Aliment> findAlimentEntities(int maxResults, int firstResult) {
        return findAlimentEntities(false, maxResults, firstResult);
    }

    private List<Aliment> findAlimentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aliment.class));
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

    public Aliment findAliment(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aliment.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlimentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aliment> rt = cq.from(Aliment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

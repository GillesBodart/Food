package bodart.food.db.controller;

import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Unit;
import bodart.food.db.entity.Aliment;
import bodart.food.db.entity.Ingredient;
import bodart.food.db.entity.Recipe;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class IngredientJpaController implements Serializable {

    public IngredientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingredient ingredient) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unit ingunit = ingredient.getIngunit();
            if (ingunit != null) {
                ingunit = em.getReference(ingunit.getClass(), ingunit.getUnitid());
                ingredient.setIngunit(ingunit);
            }
            Aliment ingali = ingredient.getIngali();
            if (ingali != null) {
                ingali = em.getReference(ingali.getClass(), ingali.getAliid());
                ingredient.setIngali(ingali);
            }
            Recipe ingrec = ingredient.getIngrec();
            if (ingrec != null) {
                ingrec = em.getReference(ingrec.getClass(), ingrec.getRecid());
                ingredient.setIngrec(ingrec);
            }
            em.persist(ingredient);
            if (ingunit != null) {
                ingunit.getIngredientList().add(ingredient);
                ingunit = em.merge(ingunit);
            }
            if (ingali != null) {
                ingali.getIngredientList().add(ingredient);
                ingali = em.merge(ingali);
            }
            if (ingrec != null) {
                ingrec.getIngredientList().add(ingredient);
                ingrec = em.merge(ingrec);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIngredient(ingredient.getIngid()) != null) {
                throw new PreexistingEntityException("Ingredient " + ingredient + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingredient ingredient) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingredient persistentIngredient = em.find(Ingredient.class, ingredient.getIngid());
            Unit ingunitOld = persistentIngredient.getIngunit();
            Unit ingunitNew = ingredient.getIngunit();
            Aliment ingaliOld = persistentIngredient.getIngali();
            Aliment ingaliNew = ingredient.getIngali();
            Recipe ingrecOld = persistentIngredient.getIngrec();
            Recipe ingrecNew = ingredient.getIngrec();
            if (ingunitNew != null) {
                ingunitNew = em.getReference(ingunitNew.getClass(), ingunitNew.getUnitid());
                ingredient.setIngunit(ingunitNew);
            }
            if (ingaliNew != null) {
                ingaliNew = em.getReference(ingaliNew.getClass(), ingaliNew.getAliid());
                ingredient.setIngali(ingaliNew);
            }
            if (ingrecNew != null) {
                ingrecNew = em.getReference(ingrecNew.getClass(), ingrecNew.getRecid());
                ingredient.setIngrec(ingrecNew);
            }
            ingredient = em.merge(ingredient);
            if (ingunitOld != null && !ingunitOld.equals(ingunitNew)) {
                ingunitOld.getIngredientList().remove(ingredient);
                ingunitOld = em.merge(ingunitOld);
            }
            if (ingunitNew != null && !ingunitNew.equals(ingunitOld)) {
                ingunitNew.getIngredientList().add(ingredient);
                ingunitNew = em.merge(ingunitNew);
            }
            if (ingaliOld != null && !ingaliOld.equals(ingaliNew)) {
                ingaliOld.getIngredientList().remove(ingredient);
                ingaliOld = em.merge(ingaliOld);
            }
            if (ingaliNew != null && !ingaliNew.equals(ingaliOld)) {
                ingaliNew.getIngredientList().add(ingredient);
                ingaliNew = em.merge(ingaliNew);
            }
            if (ingrecOld != null && !ingrecOld.equals(ingrecNew)) {
                ingrecOld.getIngredientList().remove(ingredient);
                ingrecOld = em.merge(ingrecOld);
            }
            if (ingrecNew != null && !ingrecNew.equals(ingrecOld)) {
                ingrecNew.getIngredientList().add(ingredient);
                ingrecNew = em.merge(ingrecNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ingredient.getIngid();
                if (findIngredient(id) == null) {
                    throw new NonexistentEntityException("The ingredient with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingredient ingredient;
            try {
                ingredient = em.getReference(Ingredient.class, id);
                ingredient.getIngid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingredient with id " + id + " no longer exists.", enfe);
            }
            Unit ingunit = ingredient.getIngunit();
            if (ingunit != null) {
                ingunit.getIngredientList().remove(ingredient);
                ingunit = em.merge(ingunit);
            }
            Aliment ingali = ingredient.getIngali();
            if (ingali != null) {
                ingali.getIngredientList().remove(ingredient);
                ingali = em.merge(ingali);
            }
            Recipe ingrec = ingredient.getIngrec();
            if (ingrec != null) {
                ingrec.getIngredientList().remove(ingredient);
                ingrec = em.merge(ingrec);
            }
            em.remove(ingredient);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ingredient> findIngredientEntities() {
        return findIngredientEntities(true, -1, -1);
    }

    public List<Ingredient> findIngredientEntities(int maxResults, int firstResult) {
        return findIngredientEntities(false, maxResults, firstResult);
    }

    private List<Ingredient> findIngredientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingredient.class));
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

    public Ingredient findIngredient(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingredient.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngredientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingredient> rt = cq.from(Ingredient.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

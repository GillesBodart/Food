package bodart.food.db.controller;

import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Orders;
import bodart.food.db.entity.Recipe;
import bodart.food.db.entity.Recipelist;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class RecipelistJpaController implements Serializable {

    public RecipelistJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recipelist recipelist) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders lstord = recipelist.getLstord();
            if (lstord != null) {
                lstord = em.getReference(lstord.getClass(), lstord.getOrdid());
                recipelist.setLstord(lstord);
            }
            Recipe lstrec = recipelist.getLstrec();
            if (lstrec != null) {
                lstrec = em.getReference(lstrec.getClass(), lstrec.getRecid());
                recipelist.setLstrec(lstrec);
            }
            em.persist(recipelist);
            if (lstord != null) {
                lstord.getRecipelistList().add(recipelist);
                lstord = em.merge(lstord);
            }
            if (lstrec != null) {
                lstrec.getRecipelistList().add(recipelist);
                lstrec = em.merge(lstrec);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRecipelist(recipelist.getLstid()) != null) {
                throw new PreexistingEntityException("Recipelist " + recipelist + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recipelist recipelist) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recipelist persistentRecipelist = em.find(Recipelist.class, recipelist.getLstid());
            Orders lstordOld = persistentRecipelist.getLstord();
            Orders lstordNew = recipelist.getLstord();
            Recipe lstrecOld = persistentRecipelist.getLstrec();
            Recipe lstrecNew = recipelist.getLstrec();
            if (lstordNew != null) {
                lstordNew = em.getReference(lstordNew.getClass(), lstordNew.getOrdid());
                recipelist.setLstord(lstordNew);
            }
            if (lstrecNew != null) {
                lstrecNew = em.getReference(lstrecNew.getClass(), lstrecNew.getRecid());
                recipelist.setLstrec(lstrecNew);
            }
            recipelist = em.merge(recipelist);
            if (lstordOld != null && !lstordOld.equals(lstordNew)) {
                lstordOld.getRecipelistList().remove(recipelist);
                lstordOld = em.merge(lstordOld);
            }
            if (lstordNew != null && !lstordNew.equals(lstordOld)) {
                lstordNew.getRecipelistList().add(recipelist);
                lstordNew = em.merge(lstordNew);
            }
            if (lstrecOld != null && !lstrecOld.equals(lstrecNew)) {
                lstrecOld.getRecipelistList().remove(recipelist);
                lstrecOld = em.merge(lstrecOld);
            }
            if (lstrecNew != null && !lstrecNew.equals(lstrecOld)) {
                lstrecNew.getRecipelistList().add(recipelist);
                lstrecNew = em.merge(lstrecNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = recipelist.getLstid();
                if (findRecipelist(id) == null) {
                    throw new NonexistentEntityException("The recipelist with id " + id + " no longer exists.");
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
            Recipelist recipelist;
            try {
                recipelist = em.getReference(Recipelist.class, id);
                recipelist.getLstid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recipelist with id " + id + " no longer exists.", enfe);
            }
            Orders lstord = recipelist.getLstord();
            if (lstord != null) {
                lstord.getRecipelistList().remove(recipelist);
                lstord = em.merge(lstord);
            }
            Recipe lstrec = recipelist.getLstrec();
            if (lstrec != null) {
                lstrec.getRecipelistList().remove(recipelist);
                lstrec = em.merge(lstrec);
            }
            em.remove(recipelist);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recipelist> findRecipelistEntities() {
        return findRecipelistEntities(true, -1, -1);
    }

    public List<Recipelist> findRecipelistEntities(int maxResults, int firstResult) {
        return findRecipelistEntities(false, maxResults, firstResult);
    }

    private List<Recipelist> findRecipelistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recipelist.class));
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

    public Recipelist findRecipelist(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recipelist.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecipelistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recipelist> rt = cq.from(Recipelist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

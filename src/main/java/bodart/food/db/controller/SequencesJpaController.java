

package bodart.food.db.controller;

import bodart.food.db.controller.exceptions.NonexistentEntityException;
import bodart.food.db.controller.exceptions.PreexistingEntityException;
import bodart.food.db.entity.Sequences;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Gilles
 */
public class SequencesJpaController implements Serializable {

    public SequencesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sequences sequences) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sequences);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSequences(sequences.getSeqName()) != null) {
                throw new PreexistingEntityException("Sequences " + sequences + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sequences sequences) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sequences = em.merge(sequences);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sequences.getSeqName();
                if (findSequences(id) == null) {
                    throw new NonexistentEntityException("The sequences with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sequences sequences;
            try {
                sequences = em.getReference(Sequences.class, id);
                sequences.getSeqName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sequences with id " + id + " no longer exists.", enfe);
            }
            em.remove(sequences);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sequences> findSequencesEntities() {
        return findSequencesEntities(true, -1, -1);
    }

    public List<Sequences> findSequencesEntities(int maxResults, int firstResult) {
        return findSequencesEntities(false, maxResults, firstResult);
    }

    private List<Sequences> findSequencesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sequences.class));
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

    public Sequences findSequences(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sequences.class, id);
        } finally {
            em.close();
        }
    }

    public int getSequencesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sequences> rt = cq.from(Sequences.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

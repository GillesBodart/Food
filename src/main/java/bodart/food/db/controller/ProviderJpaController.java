package bodart.food.db.controller;

import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Aliment;
import bodart.food.db.entity.Provider;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class ProviderJpaController implements Serializable {

    public ProviderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Provider provider) throws PreexistingEntityException, Exception {
        if (provider.getAlimentList() == null) {
            provider.setAlimentList(new ArrayList<Aliment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Aliment> attachedAlimentList = new ArrayList<Aliment>();
            for (Aliment alimentListAlimentToAttach : provider.getAlimentList()) {
                alimentListAlimentToAttach = em.getReference(alimentListAlimentToAttach.getClass(), alimentListAlimentToAttach.getAliid());
                attachedAlimentList.add(alimentListAlimentToAttach);
            }
            provider.setAlimentList(attachedAlimentList);
            em.persist(provider);
            for (Aliment alimentListAliment : provider.getAlimentList()) {
                Provider oldAliproviderOfAlimentListAliment = alimentListAliment.getAliprovider();
                alimentListAliment.setAliprovider(provider);
                alimentListAliment = em.merge(alimentListAliment);
                if (oldAliproviderOfAlimentListAliment != null) {
                    oldAliproviderOfAlimentListAliment.getAlimentList().remove(alimentListAliment);
                    oldAliproviderOfAlimentListAliment = em.merge(oldAliproviderOfAlimentListAliment);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProvider(provider.getProid()) != null) {
                throw new PreexistingEntityException("Provider " + provider + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Provider provider) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider persistentProvider = em.find(Provider.class, provider.getProid());
            List<Aliment> alimentListOld = persistentProvider.getAlimentList();
            List<Aliment> alimentListNew = provider.getAlimentList();
            List<Aliment> attachedAlimentListNew = new ArrayList<Aliment>();
            for (Aliment alimentListNewAlimentToAttach : alimentListNew) {
                alimentListNewAlimentToAttach = em.getReference(alimentListNewAlimentToAttach.getClass(), alimentListNewAlimentToAttach.getAliid());
                attachedAlimentListNew.add(alimentListNewAlimentToAttach);
            }
            alimentListNew = attachedAlimentListNew;
            provider.setAlimentList(alimentListNew);
            provider = em.merge(provider);
            for (Aliment alimentListOldAliment : alimentListOld) {
                if (!alimentListNew.contains(alimentListOldAliment)) {
                    alimentListOldAliment.setAliprovider(null);
                    alimentListOldAliment = em.merge(alimentListOldAliment);
                }
            }
            for (Aliment alimentListNewAliment : alimentListNew) {
                if (!alimentListOld.contains(alimentListNewAliment)) {
                    Provider oldAliproviderOfAlimentListNewAliment = alimentListNewAliment.getAliprovider();
                    alimentListNewAliment.setAliprovider(provider);
                    alimentListNewAliment = em.merge(alimentListNewAliment);
                    if (oldAliproviderOfAlimentListNewAliment != null && !oldAliproviderOfAlimentListNewAliment.equals(provider)) {
                        oldAliproviderOfAlimentListNewAliment.getAlimentList().remove(alimentListNewAliment);
                        oldAliproviderOfAlimentListNewAliment = em.merge(oldAliproviderOfAlimentListNewAliment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = provider.getProid();
                if (findProvider(id) == null) {
                    throw new NonexistentEntityException("The provider with id " + id + " no longer exists.");
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
            Provider provider;
            try {
                provider = em.getReference(Provider.class, id);
                provider.getProid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provider with id " + id + " no longer exists.", enfe);
            }
            List<Aliment> alimentList = provider.getAlimentList();
            for (Aliment alimentListAliment : alimentList) {
                alimentListAliment.setAliprovider(null);
                alimentListAliment = em.merge(alimentListAliment);
            }
            em.remove(provider);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Provider> findProviderEntities() {
        return findProviderEntities(true, -1, -1);
    }

    public List<Provider> findProviderEntities(int maxResults, int firstResult) {
        return findProviderEntities(false, maxResults, firstResult);
    }

    private List<Provider> findProviderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Provider.class));
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

    public Provider findProvider(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provider.class, id);
        } finally {
            em.close();
        }
    }

    public int getProviderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Provider> rt = cq.from(Provider.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

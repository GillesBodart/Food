

package bodart.food.db.controller;

import bodart.food.db.controller.exceptions.IllegalOrphanException;
import bodart.food.db.controller.exceptions.NonexistentEntityException;
import bodart.food.db.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Fooduser;
import bodart.food.db.entity.Orders;
import bodart.food.db.entity.Recipelist;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class OrdersJpaController implements Serializable {

    public OrdersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orders orders) throws PreexistingEntityException, Exception {
        if (orders.getRecipelistList() == null) {
            orders.setRecipelistList(new ArrayList<Recipelist>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fooduser ordowner = orders.getOrdowner();
            if (ordowner != null) {
                ordowner = em.getReference(ordowner.getClass(), ordowner.getUsrid());
                orders.setOrdowner(ordowner);
            }
            List<Recipelist> attachedRecipelistList = new ArrayList<Recipelist>();
            for (Recipelist recipelistListRecipelistToAttach : orders.getRecipelistList()) {
                recipelistListRecipelistToAttach = em.getReference(recipelistListRecipelistToAttach.getClass(), recipelistListRecipelistToAttach.getLstid());
                attachedRecipelistList.add(recipelistListRecipelistToAttach);
            }
            orders.setRecipelistList(attachedRecipelistList);
            em.persist(orders);
            if (ordowner != null) {
                ordowner.getOrdersList().add(orders);
                ordowner = em.merge(ordowner);
            }
            for (Recipelist recipelistListRecipelist : orders.getRecipelistList()) {
                Orders oldLstordOfRecipelistListRecipelist = recipelistListRecipelist.getLstord();
                recipelistListRecipelist.setLstord(orders);
                recipelistListRecipelist = em.merge(recipelistListRecipelist);
                if (oldLstordOfRecipelistListRecipelist != null) {
                    oldLstordOfRecipelistListRecipelist.getRecipelistList().remove(recipelistListRecipelist);
                    oldLstordOfRecipelistListRecipelist = em.merge(oldLstordOfRecipelistListRecipelist);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrders(orders.getOrdid()) != null) {
                throw new PreexistingEntityException("Orders " + orders + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orders orders) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders persistentOrders = em.find(Orders.class, orders.getOrdid());
            Fooduser ordownerOld = persistentOrders.getOrdowner();
            Fooduser ordownerNew = orders.getOrdowner();
            List<Recipelist> recipelistListOld = persistentOrders.getRecipelistList();
            List<Recipelist> recipelistListNew = orders.getRecipelistList();
            List<String> illegalOrphanMessages = null;
            for (Recipelist recipelistListOldRecipelist : recipelistListOld) {
                if (!recipelistListNew.contains(recipelistListOldRecipelist)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recipelist " + recipelistListOldRecipelist + " since its lstord field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ordownerNew != null) {
                ordownerNew = em.getReference(ordownerNew.getClass(), ordownerNew.getUsrid());
                orders.setOrdowner(ordownerNew);
            }
            List<Recipelist> attachedRecipelistListNew = new ArrayList<Recipelist>();
            for (Recipelist recipelistListNewRecipelistToAttach : recipelistListNew) {
                recipelistListNewRecipelistToAttach = em.getReference(recipelistListNewRecipelistToAttach.getClass(), recipelistListNewRecipelistToAttach.getLstid());
                attachedRecipelistListNew.add(recipelistListNewRecipelistToAttach);
            }
            recipelistListNew = attachedRecipelistListNew;
            orders.setRecipelistList(recipelistListNew);
            orders = em.merge(orders);
            if (ordownerOld != null && !ordownerOld.equals(ordownerNew)) {
                ordownerOld.getOrdersList().remove(orders);
                ordownerOld = em.merge(ordownerOld);
            }
            if (ordownerNew != null && !ordownerNew.equals(ordownerOld)) {
                ordownerNew.getOrdersList().add(orders);
                ordownerNew = em.merge(ordownerNew);
            }
            for (Recipelist recipelistListNewRecipelist : recipelistListNew) {
                if (!recipelistListOld.contains(recipelistListNewRecipelist)) {
                    Orders oldLstordOfRecipelistListNewRecipelist = recipelistListNewRecipelist.getLstord();
                    recipelistListNewRecipelist.setLstord(orders);
                    recipelistListNewRecipelist = em.merge(recipelistListNewRecipelist);
                    if (oldLstordOfRecipelistListNewRecipelist != null && !oldLstordOfRecipelistListNewRecipelist.equals(orders)) {
                        oldLstordOfRecipelistListNewRecipelist.getRecipelistList().remove(recipelistListNewRecipelist);
                        oldLstordOfRecipelistListNewRecipelist = em.merge(oldLstordOfRecipelistListNewRecipelist);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = orders.getOrdid();
                if (findOrders(id) == null) {
                    throw new NonexistentEntityException("The orders with id " + id + " no longer exists.");
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
            Orders orders;
            try {
                orders = em.getReference(Orders.class, id);
                orders.getOrdid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orders with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Recipelist> recipelistListOrphanCheck = orders.getRecipelistList();
            for (Recipelist recipelistListOrphanCheckRecipelist : recipelistListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orders (" + orders + ") cannot be destroyed since the Recipelist " + recipelistListOrphanCheckRecipelist + " in its recipelistList field has a non-nullable lstord field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Fooduser ordowner = orders.getOrdowner();
            if (ordowner != null) {
                ordowner.getOrdersList().remove(orders);
                ordowner = em.merge(ordowner);
            }
            em.remove(orders);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orders> findOrdersEntities() {
        return findOrdersEntities(true, -1, -1);
    }

    public List<Orders> findOrdersEntities(int maxResults, int firstResult) {
        return findOrdersEntities(false, maxResults, firstResult);
    }

    private List<Orders> findOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orders.class));
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

    public Orders findOrders(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orders.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orders> rt = cq.from(Orders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

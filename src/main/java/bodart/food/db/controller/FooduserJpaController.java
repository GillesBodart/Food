package bodart.food.db.controller;

import bodart.food.db.exceptions.IllegalOrphanException;
import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import bodart.food.db.entity.Fooduser;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Orders;
import java.util.ArrayList;
import java.util.List;
import bodart.food.db.entity.Recipe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class FooduserJpaController implements Serializable {

    public FooduserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fooduser fooduser) throws PreexistingEntityException, Exception {
        if (fooduser.getOrdersList() == null) {
            fooduser.setOrdersList(new ArrayList<Orders>());
        }
        if (fooduser.getRecipeList() == null) {
            fooduser.setRecipeList(new ArrayList<Recipe>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Orders> attachedOrdersList = new ArrayList<Orders>();
            for (Orders ordersListOrdersToAttach : fooduser.getOrdersList()) {
                ordersListOrdersToAttach = em.getReference(ordersListOrdersToAttach.getClass(), ordersListOrdersToAttach.getOrdid());
                attachedOrdersList.add(ordersListOrdersToAttach);
            }
            fooduser.setOrdersList(attachedOrdersList);
            List<Recipe> attachedRecipeList = new ArrayList<Recipe>();
            for (Recipe recipeListRecipeToAttach : fooduser.getRecipeList()) {
                recipeListRecipeToAttach = em.getReference(recipeListRecipeToAttach.getClass(), recipeListRecipeToAttach.getRecid());
                attachedRecipeList.add(recipeListRecipeToAttach);
            }
            fooduser.setRecipeList(attachedRecipeList);
            em.persist(fooduser);
            for (Orders ordersListOrders : fooduser.getOrdersList()) {
                Fooduser oldOrdownerOfOrdersListOrders = ordersListOrders.getOrdowner();
                ordersListOrders.setOrdowner(fooduser);
                ordersListOrders = em.merge(ordersListOrders);
                if (oldOrdownerOfOrdersListOrders != null) {
                    oldOrdownerOfOrdersListOrders.getOrdersList().remove(ordersListOrders);
                    oldOrdownerOfOrdersListOrders = em.merge(oldOrdownerOfOrdersListOrders);
                }
            }
            for (Recipe recipeListRecipe : fooduser.getRecipeList()) {
                Fooduser oldRecownerOfRecipeListRecipe = recipeListRecipe.getRecowner();
                recipeListRecipe.setRecowner(fooduser);
                recipeListRecipe = em.merge(recipeListRecipe);
                if (oldRecownerOfRecipeListRecipe != null) {
                    oldRecownerOfRecipeListRecipe.getRecipeList().remove(recipeListRecipe);
                    oldRecownerOfRecipeListRecipe = em.merge(oldRecownerOfRecipeListRecipe);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFooduser(fooduser.getUsrid()) != null) {
                throw new PreexistingEntityException("Fooduser " + fooduser + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fooduser fooduser) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fooduser persistentFooduser = em.find(Fooduser.class, fooduser.getUsrid());
            List<Orders> ordersListOld = persistentFooduser.getOrdersList();
            List<Orders> ordersListNew = fooduser.getOrdersList();
            List<Recipe> recipeListOld = persistentFooduser.getRecipeList();
            List<Recipe> recipeListNew = fooduser.getRecipeList();
            List<String> illegalOrphanMessages = null;
            for (Orders ordersListOldOrders : ordersListOld) {
                if (!ordersListNew.contains(ordersListOldOrders)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orders " + ordersListOldOrders + " since its ordowner field is not nullable.");
                }
            }
            for (Recipe recipeListOldRecipe : recipeListOld) {
                if (!recipeListNew.contains(recipeListOldRecipe)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recipe " + recipeListOldRecipe + " since its recowner field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Orders> attachedOrdersListNew = new ArrayList<Orders>();
            for (Orders ordersListNewOrdersToAttach : ordersListNew) {
                ordersListNewOrdersToAttach = em.getReference(ordersListNewOrdersToAttach.getClass(), ordersListNewOrdersToAttach.getOrdid());
                attachedOrdersListNew.add(ordersListNewOrdersToAttach);
            }
            ordersListNew = attachedOrdersListNew;
            fooduser.setOrdersList(ordersListNew);
            List<Recipe> attachedRecipeListNew = new ArrayList<Recipe>();
            for (Recipe recipeListNewRecipeToAttach : recipeListNew) {
                recipeListNewRecipeToAttach = em.getReference(recipeListNewRecipeToAttach.getClass(), recipeListNewRecipeToAttach.getRecid());
                attachedRecipeListNew.add(recipeListNewRecipeToAttach);
            }
            recipeListNew = attachedRecipeListNew;
            fooduser.setRecipeList(recipeListNew);
            fooduser = em.merge(fooduser);
            for (Orders ordersListNewOrders : ordersListNew) {
                if (!ordersListOld.contains(ordersListNewOrders)) {
                    Fooduser oldOrdownerOfOrdersListNewOrders = ordersListNewOrders.getOrdowner();
                    ordersListNewOrders.setOrdowner(fooduser);
                    ordersListNewOrders = em.merge(ordersListNewOrders);
                    if (oldOrdownerOfOrdersListNewOrders != null && !oldOrdownerOfOrdersListNewOrders.equals(fooduser)) {
                        oldOrdownerOfOrdersListNewOrders.getOrdersList().remove(ordersListNewOrders);
                        oldOrdownerOfOrdersListNewOrders = em.merge(oldOrdownerOfOrdersListNewOrders);
                    }
                }
            }
            for (Recipe recipeListNewRecipe : recipeListNew) {
                if (!recipeListOld.contains(recipeListNewRecipe)) {
                    Fooduser oldRecownerOfRecipeListNewRecipe = recipeListNewRecipe.getRecowner();
                    recipeListNewRecipe.setRecowner(fooduser);
                    recipeListNewRecipe = em.merge(recipeListNewRecipe);
                    if (oldRecownerOfRecipeListNewRecipe != null && !oldRecownerOfRecipeListNewRecipe.equals(fooduser)) {
                        oldRecownerOfRecipeListNewRecipe.getRecipeList().remove(recipeListNewRecipe);
                        oldRecownerOfRecipeListNewRecipe = em.merge(oldRecownerOfRecipeListNewRecipe);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fooduser.getUsrid();
                if (findFooduser(id) == null) {
                    throw new NonexistentEntityException("The fooduser with id " + id + " no longer exists.");
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
            Fooduser fooduser;
            try {
                fooduser = em.getReference(Fooduser.class, id);
                fooduser.getUsrid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fooduser with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orders> ordersListOrphanCheck = fooduser.getOrdersList();
            for (Orders ordersListOrphanCheckOrders : ordersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Fooduser (" + fooduser + ") cannot be destroyed since the Orders " + ordersListOrphanCheckOrders + " in its ordersList field has a non-nullable ordowner field.");
            }
            List<Recipe> recipeListOrphanCheck = fooduser.getRecipeList();
            for (Recipe recipeListOrphanCheckRecipe : recipeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Fooduser (" + fooduser + ") cannot be destroyed since the Recipe " + recipeListOrphanCheckRecipe + " in its recipeList field has a non-nullable recowner field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(fooduser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fooduser> findFooduserEntities() {
        return findFooduserEntities(true, -1, -1);
    }

    public List<Fooduser> findFooduserEntities(int maxResults, int firstResult) {
        return findFooduserEntities(false, maxResults, firstResult);
    }

    private List<Fooduser> findFooduserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fooduser.class));
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

    public Fooduser findFooduser(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fooduser.class, id);
        } finally {
            em.close();
        }
    }

    public int getFooduserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fooduser> rt = cq.from(Fooduser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

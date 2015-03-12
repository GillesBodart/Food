package bodart.food.db.controller;

import bodart.food.db.exceptions.IllegalOrphanException;
import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Fooduser;
import bodart.food.db.entity.Ingredient;
import bodart.food.db.entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import bodart.food.db.entity.Recipelist;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class RecipeJpaController implements Serializable {

    public RecipeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recipe recipe) throws PreexistingEntityException, Exception {
        if (recipe.getIngredientList() == null) {
            recipe.setIngredientList(new ArrayList<Ingredient>());
        }
        if (recipe.getRecipelistList() == null) {
            recipe.setRecipelistList(new ArrayList<Recipelist>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fooduser recowner = recipe.getRecowner();
            if (recowner != null) {
                recowner = em.getReference(recowner.getClass(), recowner.getUsrid());
                recipe.setRecowner(recowner);
            }
            List<Ingredient> attachedIngredientList = new ArrayList<Ingredient>();
            for (Ingredient ingredientListIngredientToAttach : recipe.getIngredientList()) {
                ingredientListIngredientToAttach = em.getReference(ingredientListIngredientToAttach.getClass(), ingredientListIngredientToAttach.getIngid());
                attachedIngredientList.add(ingredientListIngredientToAttach);
            }
            recipe.setIngredientList(attachedIngredientList);
            List<Recipelist> attachedRecipelistList = new ArrayList<Recipelist>();
            for (Recipelist recipelistListRecipelistToAttach : recipe.getRecipelistList()) {
                recipelistListRecipelistToAttach = em.getReference(recipelistListRecipelistToAttach.getClass(), recipelistListRecipelistToAttach.getLstid());
                attachedRecipelistList.add(recipelistListRecipelistToAttach);
            }
            recipe.setRecipelistList(attachedRecipelistList);
            em.persist(recipe);
            if (recowner != null) {
                recowner.getRecipeList().add(recipe);
                recowner = em.merge(recowner);
            }
            for (Ingredient ingredientListIngredient : recipe.getIngredientList()) {
                Recipe oldIngrecOfIngredientListIngredient = ingredientListIngredient.getIngrec();
                ingredientListIngredient.setIngrec(recipe);
                ingredientListIngredient = em.merge(ingredientListIngredient);
                if (oldIngrecOfIngredientListIngredient != null) {
                    oldIngrecOfIngredientListIngredient.getIngredientList().remove(ingredientListIngredient);
                    oldIngrecOfIngredientListIngredient = em.merge(oldIngrecOfIngredientListIngredient);
                }
            }
            for (Recipelist recipelistListRecipelist : recipe.getRecipelistList()) {
                Recipe oldLstrecOfRecipelistListRecipelist = recipelistListRecipelist.getLstrec();
                recipelistListRecipelist.setLstrec(recipe);
                recipelistListRecipelist = em.merge(recipelistListRecipelist);
                if (oldLstrecOfRecipelistListRecipelist != null) {
                    oldLstrecOfRecipelistListRecipelist.getRecipelistList().remove(recipelistListRecipelist);
                    oldLstrecOfRecipelistListRecipelist = em.merge(oldLstrecOfRecipelistListRecipelist);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRecipe(recipe.getRecid()) != null) {
                throw new PreexistingEntityException("Recipe " + recipe + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recipe recipe) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recipe persistentRecipe = em.find(Recipe.class, recipe.getRecid());
            Fooduser recownerOld = persistentRecipe.getRecowner();
            Fooduser recownerNew = recipe.getRecowner();
            List<Ingredient> ingredientListOld = persistentRecipe.getIngredientList();
            List<Ingredient> ingredientListNew = recipe.getIngredientList();
            List<Recipelist> recipelistListOld = persistentRecipe.getRecipelistList();
            List<Recipelist> recipelistListNew = recipe.getRecipelistList();
            List<String> illegalOrphanMessages = null;
            for (Ingredient ingredientListOldIngredient : ingredientListOld) {
                if (!ingredientListNew.contains(ingredientListOldIngredient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ingredient " + ingredientListOldIngredient + " since its ingrec field is not nullable.");
                }
            }
            for (Recipelist recipelistListOldRecipelist : recipelistListOld) {
                if (!recipelistListNew.contains(recipelistListOldRecipelist)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recipelist " + recipelistListOldRecipelist + " since its lstrec field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (recownerNew != null) {
                recownerNew = em.getReference(recownerNew.getClass(), recownerNew.getUsrid());
                recipe.setRecowner(recownerNew);
            }
            List<Ingredient> attachedIngredientListNew = new ArrayList<Ingredient>();
            for (Ingredient ingredientListNewIngredientToAttach : ingredientListNew) {
                ingredientListNewIngredientToAttach = em.getReference(ingredientListNewIngredientToAttach.getClass(), ingredientListNewIngredientToAttach.getIngid());
                attachedIngredientListNew.add(ingredientListNewIngredientToAttach);
            }
            ingredientListNew = attachedIngredientListNew;
            recipe.setIngredientList(ingredientListNew);
            List<Recipelist> attachedRecipelistListNew = new ArrayList<Recipelist>();
            for (Recipelist recipelistListNewRecipelistToAttach : recipelistListNew) {
                recipelistListNewRecipelistToAttach = em.getReference(recipelistListNewRecipelistToAttach.getClass(), recipelistListNewRecipelistToAttach.getLstid());
                attachedRecipelistListNew.add(recipelistListNewRecipelistToAttach);
            }
            recipelistListNew = attachedRecipelistListNew;
            recipe.setRecipelistList(recipelistListNew);
            recipe = em.merge(recipe);
            if (recownerOld != null && !recownerOld.equals(recownerNew)) {
                recownerOld.getRecipeList().remove(recipe);
                recownerOld = em.merge(recownerOld);
            }
            if (recownerNew != null && !recownerNew.equals(recownerOld)) {
                recownerNew.getRecipeList().add(recipe);
                recownerNew = em.merge(recownerNew);
            }
            for (Ingredient ingredientListNewIngredient : ingredientListNew) {
                if (!ingredientListOld.contains(ingredientListNewIngredient)) {
                    Recipe oldIngrecOfIngredientListNewIngredient = ingredientListNewIngredient.getIngrec();
                    ingredientListNewIngredient.setIngrec(recipe);
                    ingredientListNewIngredient = em.merge(ingredientListNewIngredient);
                    if (oldIngrecOfIngredientListNewIngredient != null && !oldIngrecOfIngredientListNewIngredient.equals(recipe)) {
                        oldIngrecOfIngredientListNewIngredient.getIngredientList().remove(ingredientListNewIngredient);
                        oldIngrecOfIngredientListNewIngredient = em.merge(oldIngrecOfIngredientListNewIngredient);
                    }
                }
            }
            for (Recipelist recipelistListNewRecipelist : recipelistListNew) {
                if (!recipelistListOld.contains(recipelistListNewRecipelist)) {
                    Recipe oldLstrecOfRecipelistListNewRecipelist = recipelistListNewRecipelist.getLstrec();
                    recipelistListNewRecipelist.setLstrec(recipe);
                    recipelistListNewRecipelist = em.merge(recipelistListNewRecipelist);
                    if (oldLstrecOfRecipelistListNewRecipelist != null && !oldLstrecOfRecipelistListNewRecipelist.equals(recipe)) {
                        oldLstrecOfRecipelistListNewRecipelist.getRecipelistList().remove(recipelistListNewRecipelist);
                        oldLstrecOfRecipelistListNewRecipelist = em.merge(oldLstrecOfRecipelistListNewRecipelist);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = recipe.getRecid();
                if (findRecipe(id) == null) {
                    throw new NonexistentEntityException("The recipe with id " + id + " no longer exists.");
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
            Recipe recipe;
            try {
                recipe = em.getReference(Recipe.class, id);
                recipe.getRecid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recipe with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ingredient> ingredientListOrphanCheck = recipe.getIngredientList();
            for (Ingredient ingredientListOrphanCheckIngredient : ingredientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recipe (" + recipe + ") cannot be destroyed since the Ingredient " + ingredientListOrphanCheckIngredient + " in its ingredientList field has a non-nullable ingrec field.");
            }
            List<Recipelist> recipelistListOrphanCheck = recipe.getRecipelistList();
            for (Recipelist recipelistListOrphanCheckRecipelist : recipelistListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recipe (" + recipe + ") cannot be destroyed since the Recipelist " + recipelistListOrphanCheckRecipelist + " in its recipelistList field has a non-nullable lstrec field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Fooduser recowner = recipe.getRecowner();
            if (recowner != null) {
                recowner.getRecipeList().remove(recipe);
                recowner = em.merge(recowner);
            }
            em.remove(recipe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recipe> findRecipeEntities() {
        return findRecipeEntities(true, -1, -1);
    }

    public List<Recipe> findRecipeEntities(int maxResults, int firstResult) {
        return findRecipeEntities(false, maxResults, firstResult);
    }

    private List<Recipe> findRecipeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recipe.class));
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

    public Recipe findRecipe(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recipe.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecipeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recipe> rt = cq.from(Recipe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

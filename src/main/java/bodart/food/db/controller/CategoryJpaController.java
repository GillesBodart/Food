package bodart.food.db.controller;

import bodart.food.db.exceptions.IllegalOrphanException;
import bodart.food.db.exceptions.NonexistentEntityException;
import bodart.food.db.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import bodart.food.db.entity.Category;
import bodart.food.db.entity.Aliment;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gilles
 */
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) throws PreexistingEntityException, Exception {
        if (category.getAlimentList() == null) {
            category.setAlimentList(new ArrayList<Aliment>());
        }
        if (category.getCategoryList() == null) {
            category.setCategoryList(new ArrayList<Category>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category catparent = category.getCatparent();
            if (catparent != null) {
                catparent = em.getReference(catparent.getClass(), catparent.getCatid());
                category.setCatparent(catparent);
            }
            List<Aliment> attachedAlimentList = new ArrayList<Aliment>();
            for (Aliment alimentListAlimentToAttach : category.getAlimentList()) {
                alimentListAlimentToAttach = em.getReference(alimentListAlimentToAttach.getClass(), alimentListAlimentToAttach.getAliid());
                attachedAlimentList.add(alimentListAlimentToAttach);
            }
            category.setAlimentList(attachedAlimentList);
            List<Category> attachedCategoryList = new ArrayList<Category>();
            for (Category categoryListCategoryToAttach : category.getCategoryList()) {
                categoryListCategoryToAttach = em.getReference(categoryListCategoryToAttach.getClass(), categoryListCategoryToAttach.getCatid());
                attachedCategoryList.add(categoryListCategoryToAttach);
            }
            category.setCategoryList(attachedCategoryList);
            em.persist(category);
            if (catparent != null) {
                catparent.getCategoryList().add(category);
                catparent = em.merge(catparent);
            }
            for (Aliment alimentListAliment : category.getAlimentList()) {
                Category oldAlicategoryOfAlimentListAliment = alimentListAliment.getAlicategory();
                alimentListAliment.setAlicategory(category);
                alimentListAliment = em.merge(alimentListAliment);
                if (oldAlicategoryOfAlimentListAliment != null) {
                    oldAlicategoryOfAlimentListAliment.getAlimentList().remove(alimentListAliment);
                    oldAlicategoryOfAlimentListAliment = em.merge(oldAlicategoryOfAlimentListAliment);
                }
            }
            for (Category categoryListCategory : category.getCategoryList()) {
                Category oldCatparentOfCategoryListCategory = categoryListCategory.getCatparent();
                categoryListCategory.setCatparent(category);
                categoryListCategory = em.merge(categoryListCategory);
                if (oldCatparentOfCategoryListCategory != null) {
                    oldCatparentOfCategoryListCategory.getCategoryList().remove(categoryListCategory);
                    oldCatparentOfCategoryListCategory = em.merge(oldCatparentOfCategoryListCategory);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategory(category.getCatid()) != null) {
                throw new PreexistingEntityException("Category " + category + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getCatid());
            Category catparentOld = persistentCategory.getCatparent();
            Category catparentNew = category.getCatparent();
            List<Aliment> alimentListOld = persistentCategory.getAlimentList();
            List<Aliment> alimentListNew = category.getAlimentList();
            List<Category> categoryListOld = persistentCategory.getCategoryList();
            List<Category> categoryListNew = category.getCategoryList();
            List<String> illegalOrphanMessages = null;
            for (Aliment alimentListOldAliment : alimentListOld) {
                if (!alimentListNew.contains(alimentListOldAliment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Aliment " + alimentListOldAliment + " since its alicategory field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (catparentNew != null) {
                catparentNew = em.getReference(catparentNew.getClass(), catparentNew.getCatid());
                category.setCatparent(catparentNew);
            }
            List<Aliment> attachedAlimentListNew = new ArrayList<Aliment>();
            for (Aliment alimentListNewAlimentToAttach : alimentListNew) {
                alimentListNewAlimentToAttach = em.getReference(alimentListNewAlimentToAttach.getClass(), alimentListNewAlimentToAttach.getAliid());
                attachedAlimentListNew.add(alimentListNewAlimentToAttach);
            }
            alimentListNew = attachedAlimentListNew;
            category.setAlimentList(alimentListNew);
            List<Category> attachedCategoryListNew = new ArrayList<Category>();
            for (Category categoryListNewCategoryToAttach : categoryListNew) {
                categoryListNewCategoryToAttach = em.getReference(categoryListNewCategoryToAttach.getClass(), categoryListNewCategoryToAttach.getCatid());
                attachedCategoryListNew.add(categoryListNewCategoryToAttach);
            }
            categoryListNew = attachedCategoryListNew;
            category.setCategoryList(categoryListNew);
            category = em.merge(category);
            if (catparentOld != null && !catparentOld.equals(catparentNew)) {
                catparentOld.getCategoryList().remove(category);
                catparentOld = em.merge(catparentOld);
            }
            if (catparentNew != null && !catparentNew.equals(catparentOld)) {
                catparentNew.getCategoryList().add(category);
                catparentNew = em.merge(catparentNew);
            }
            for (Aliment alimentListNewAliment : alimentListNew) {
                if (!alimentListOld.contains(alimentListNewAliment)) {
                    Category oldAlicategoryOfAlimentListNewAliment = alimentListNewAliment.getAlicategory();
                    alimentListNewAliment.setAlicategory(category);
                    alimentListNewAliment = em.merge(alimentListNewAliment);
                    if (oldAlicategoryOfAlimentListNewAliment != null && !oldAlicategoryOfAlimentListNewAliment.equals(category)) {
                        oldAlicategoryOfAlimentListNewAliment.getAlimentList().remove(alimentListNewAliment);
                        oldAlicategoryOfAlimentListNewAliment = em.merge(oldAlicategoryOfAlimentListNewAliment);
                    }
                }
            }
            for (Category categoryListOldCategory : categoryListOld) {
                if (!categoryListNew.contains(categoryListOldCategory)) {
                    categoryListOldCategory.setCatparent(null);
                    categoryListOldCategory = em.merge(categoryListOldCategory);
                }
            }
            for (Category categoryListNewCategory : categoryListNew) {
                if (!categoryListOld.contains(categoryListNewCategory)) {
                    Category oldCatparentOfCategoryListNewCategory = categoryListNewCategory.getCatparent();
                    categoryListNewCategory.setCatparent(category);
                    categoryListNewCategory = em.merge(categoryListNewCategory);
                    if (oldCatparentOfCategoryListNewCategory != null && !oldCatparentOfCategoryListNewCategory.equals(category)) {
                        oldCatparentOfCategoryListNewCategory.getCategoryList().remove(categoryListNewCategory);
                        oldCatparentOfCategoryListNewCategory = em.merge(oldCatparentOfCategoryListNewCategory);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = category.getCatid();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getCatid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Aliment> alimentListOrphanCheck = category.getAlimentList();
            for (Aliment alimentListOrphanCheckAliment : alimentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the Aliment " + alimentListOrphanCheckAliment + " in its alimentList field has a non-nullable alicategory field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Category catparent = category.getCatparent();
            if (catparent != null) {
                catparent.getCategoryList().remove(category);
                catparent = em.merge(catparent);
            }
            List<Category> categoryList = category.getCategoryList();
            for (Category categoryListCategory : categoryList) {
                categoryListCategory.setCatparent(null);
                categoryListCategory = em.merge(categoryListCategory);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

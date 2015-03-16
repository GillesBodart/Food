/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodart.food.db.controller;

import bodart.food.db.entity.Category;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Gilles
 */
public class CategoryCtrl {
    
     @PersistenceContext
    private EntityManager em;
    
    private CategoryCtrl() {
    }
    
    public static CategoryCtrl getInstance() {
        return CategoryCtrlHolder.INSTANCE;
    }
    
    private static class CategoryCtrlHolder {

        private static final CategoryCtrl INSTANCE = new CategoryCtrl();
    }
    
    public String addCategory(String name){
        Category cat = new Category(new Long(0),name);
        em.persist(cat);
        return "Category added";
    }
    public String addSubCategory(String name, Category parent){
        Category cat = new Category(new Long(0),name);
        cat.setCatparent(parent);
        em.persist(cat);
        return "Sub-Category added";
    }
}

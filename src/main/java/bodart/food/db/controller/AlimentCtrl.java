/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodart.food.db.controller;

import bodart.food.db.entity.Aliment;
import bodart.food.db.entity.Category;
import bodart.food.db.entity.Provider;
import bodart.food.db.entity.Unit;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Gilles
 */
public class AlimentCtrl {

    @PersistenceContext
    private EntityManager em;

    private AlimentCtrl() {
    }

    public static AlimentCtrl getInstance() {
        return AlimentCtrlHolder.INSTANCE;
    }

    private static class AlimentCtrlHolder {

        private static final AlimentCtrl INSTANCE = new AlimentCtrl();
    }

    public String addAliment(String name, BigDecimal price, Provider provider, Unit unit, Category cate) {
        Aliment ali = new Aliment(new Long(0), name, price);
        ali.setAliprovider(provider);
        ali.setAliunit(unit);
        ali.setAlicategory(cate);
        em.persist(ali);
        return "Aliment added";
    }

    public String updateAliment(long id, String name, BigDecimal price, Provider provider, Unit unit, Category cate) {
        Aliment ali = em.find(Aliment.class, id);
        if (name != null) {
            ali.setAliname(name);
        }
        if (price != null) {
            ali.setAliprix(price);
        }
        if (provider != null) {
            ali.setAliprovider(provider);
        };
        if (unit != null) {
            ali.setAliunit(unit);
        }
        if (cate != null) {
            ali.setAlicategory(cate);
        }
        em.persist(ali);
        return "Aliment updated";
    }

}

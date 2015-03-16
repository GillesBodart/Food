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
public class ProviderCtrl {

    @PersistenceContext
    private EntityManager em;

    private ProviderCtrl() {
    }

    public static ProviderCtrl getInstance() {
        return ProviderCtrlHolder.INSTANCE;
    }

    private static class ProviderCtrlHolder {

        private static final ProviderCtrl INSTANCE = new ProviderCtrl();
    }

    public String addProvider(String name, String address){
        Provider pro = new Provider(new Long(0),name,address);
        em.persist(pro);
        return "Provider created";
    }
    public String updateProvider(long id,String name, String address){
        Provider pro = em.find(Provider.class, id);
        if (null != name) pro.setProname(name);
        if (null != address) pro.setProname(address);
        em.persist(pro);
        return "Provider updated";
    }

}

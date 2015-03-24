/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodart.food.db.controller;

import bodart.food.db.entity.Unit;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Gilles
 */
public class UnitCtrl {
    
    @PersistenceContext
    private EntityManager em;
    
    private UnitCtrl() {
    }
    
    public static UnitCtrl getInstance() {
        return UnitCtrlHolder.INSTANCE;
    }
    
    private static class UnitCtrlHolder {

        private static final UnitCtrl INSTANCE = new UnitCtrl();
    }
    
    public String addProvider(String name, BigDecimal report){
        Unit unit = new Unit(new Long(0),name,report);
        em.persist(unit);
        return "Provider created";
    }
    public String updateProvider(long id,String name, BigDecimal report){
        Unit unit = em.find(Unit.class, id);
        if (null != name) unit.setUnitname(name);
        if (null != report) unit.setUnitreport(report);
        em.persist(unit);
        return "Provider updated";
    }
}

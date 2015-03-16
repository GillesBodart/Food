/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodart.food.db.controller;

/**
 *
 * @author Gilles
 */
public class UnitCtrl {
    
    private UnitCtrl() {
    }
    
    public static UnitCtrl getInstance() {
        return UnitCtrlHolder.INSTANCE;
    }
    
    private static class UnitCtrlHolder {

        private static final UnitCtrl INSTANCE = new UnitCtrl();
    }
}

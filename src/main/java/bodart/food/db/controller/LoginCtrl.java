package bodart.food.db.controller;

import bodart.food.db.entity.Fooduser;
import bodart.food.db.exceptions.FoodMajorException;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gilles
 */
public class LoginCtrl {

    @PersistenceContext
    private EntityManager em;

    private LoginCtrl() {
    }

    public static LoginCtrl getInstance() {
        return LoginCtrlHolder.INSTANCE;
    }

    private static class LoginCtrlHolder {

        private static final LoginCtrl INSTANCE = new LoginCtrl();
    }

    public String logIn(String email) throws FoodMajorException {
        Query qry = em.createNamedQuery("Fooduser.findByUsremail");
        qry.setParameter("usremail", email);
        Fooduser user = (Fooduser) qry.getSingleResult();

        String uniqueID = UUID.randomUUID().toString();
        if (null == user) {
            Logger.logMsg(Logger.ERROR, "User loged fail : " + email);
            throw new FoodMajorException("bad id");
        } else {
            Logger.logMsg(Logger.INFO, "User loged in : " + user.toString());
            return uniqueID;
        }
    }
    
    public String logOut(String email) throws FoodMajorException {
        Query qry = em.createNamedQuery("Fooduser.findByUsremail");
        qry.setParameter("usremail", email);
        Fooduser user = (Fooduser) qry.getSingleResult();

        String uniqueID = UUID.randomUUID().toString();
        if (null == user) {
            Logger.logMsg(Logger.ERROR, "User loged fail : " + email);
            throw new FoodMajorException("bad id");
        } else {
            Logger.logMsg(Logger.INFO, "User loged in : " + user.toString());
            return uniqueID;
        }
    }

    public boolean createUser(String fName, String lName, String email, String tel, String pass, String address) throws FoodMajorException, NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytesOfMessage = pass.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        BigInteger bigInt = new BigInteger(1, thedigest);
        String hashtext = bigInt.toString(16);
        Fooduser user = new Fooduser(new Long(0), fName, lName, hashtext, address, email, tel);
        em.persist(user);
        Logger.logMsg(Logger.INFO, "User created : " + user.toString());
        return true;
    }

}

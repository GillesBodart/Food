package bodart.food.db.controller;

import bodart.food.db.entity.Fooduser;
import bodart.food.db.exceptions.FoodMajorException;
import bodart.food.db.exceptions.FoodMinorException;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
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
    private static Map<String,Fooduser> connectedUser;

    private LoginCtrl() {
        connectedUser=new HashMap<>();
    }

    public static LoginCtrl getInstance() {
        return LoginCtrlHolder.INSTANCE;
    }

    private static class LoginCtrlHolder {
        private static final LoginCtrl INSTANCE = new LoginCtrl();
    }

    public String logIn(String email,String pass) throws FoodMajorException, UnsupportedEncodingException, NoSuchAlgorithmException {
        Query qry = em.createNamedQuery("Fooduser.login");
        qry.setParameter("usremail", email);
        byte[] bytesOfMessage = pass.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        BigInteger bigInt = new BigInteger(1, thedigest);
        String hashtext = bigInt.toString(16);
        qry.setParameter("usrpassword", hashtext);
        Fooduser user = (Fooduser) qry.getSingleResult();
        String uniqueID = UUID.randomUUID().toString();
        if (null == user) {
            Logger.logMsg(Logger.ERROR, "User loged fail : " + email);
            throw new FoodMajorException("bad id");
        } else {
            Logger.logMsg(Logger.INFO, "User loged in : " + user.toString());
            connectedUser.put(uniqueID,user);
            return uniqueID;
        }
    }
    
    public void logOut(String token) throws FoodMinorException {
        if (!connectedUser.containsKey(token)) {
            Logger.logMsg(Logger.ERROR, "User logOut fail : " + token +" not present in the list");
            throw new FoodMinorException("User logOut fail : " + token +" not present in the list");
        } else {
            connectedUser.remove(token);
            Logger.logMsg(Logger.INFO, "User loged out : " + token);
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

    public int connectedPoeple(){
        return connectedUser.size();
    }
    
}

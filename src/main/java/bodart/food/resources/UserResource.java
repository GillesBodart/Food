package bodart.food.resources;

import bodart.food.db.controller.LoginCtrl;
import bodart.food.db.exceptions.FoodMajorException;
import bodart.food.db.exceptions.FoodMinorException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/user")
public class UserResource {

    private final String sep = System.getProperty("file.separator");
    private final LoginCtrl logCtrl;

    public UserResource() {
        logCtrl = LoginCtrl.getInstance();
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @FormDataParam("email") String mail,@FormDataParam("password") String password) {
        try {
            String token = logCtrl.logIn(mail, password);
            String message = "Loged in";
            return Response
                    .ok(message)
                    .header("Food-token", token)
                    .build();
        } catch (FoodMajorException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(
            @HeaderParam("Food-token") String token) {
        try {
            logCtrl.logOut(token);
            String message = "Loged out";
            return Response
                    .ok(message)
                    .build();
        } catch (FoodMinorException ex) {
            Logger.getLogger(UserResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/subscribe")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(@FormDataParam("firstName") String fName,
            @FormDataParam("lastName") String lName,
            @FormDataParam("email") String email,
            @FormDataParam("tel") String tel,
            @FormDataParam("password") String pass,
            @FormDataParam("adresse") String address) {
        try {
            String message = "user ";
            message = message + (logCtrl.createUser(fName, lName, email, tel, pass, address) ? " created" :" not created");
            return Response
                    .ok(message)
                    .build();
        } catch (FoodMajorException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(UserResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response
                    .serverError()
                    .build();
        }
    }
}

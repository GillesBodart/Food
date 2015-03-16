package bodart.food.resources;

import bodart.food.db.controller.LoginCtrl;
import bodart.food.db.exceptions.FoodMajorException;
import bodart.food.db.exceptions.FoodMinorException;
import com.sun.jersey.multipart.FormDataParam;
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

@Path("/aliment")
public class AlimentResource {

    private final String sep = System.getProperty("file.separator");
    private final LoginCtrl logCtrl;

    public AlimentResource() {
        logCtrl = LoginCtrl.getInstance();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(
            @FormDataParam("email") String mail,@FormDataParam("password") String password) {
        try {
            String token = logCtrl.logIn(mail, password);
            String message = "Loged in";
            return Response
                    .ok(message)
                    .header("Food-token", token)
                    .build();
        } catch (FoodMajorException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(AlimentResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @HeaderParam("Food-token") String token) {
        try {
            logCtrl.logOut(token);
            String message = "Loged out";
            return Response
                    .ok(message)
                    .build();
        } catch (FoodMinorException ex) {
            Logger.getLogger(AlimentResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
            @HeaderParam("Food-token") String token) {
        try {
            logCtrl.logOut(token);
            String message = "Loged out";
            return Response
                    .ok(message)
                    .build();
        } catch (FoodMinorException ex) {
            Logger.getLogger(AlimentResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    
}

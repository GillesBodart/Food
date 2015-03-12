package bodart.food.resources;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {

    private final String SERVER_DOWNLOAD_LOCATION_FOLDER = "D:\\tmp\\";
    private final String sep = System.getProperty("file.separator");

    public UserResource() {

    }

    @GET
    @Path("/{email}/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @PathParam("email") String uniqueID) {
        String token = "token";
        String message = "Loged in";
        return Response
                .ok(message)
                .header("Food-token", token)
                .build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(@FormDataParam("firstName") String fName,
            @FormDataParam("lastName") String lName,
            @FormDataParam("email") String email,
            @FormDataParam("tel") String tel,
            @FormDataParam("password") String pass,
            @FormDataParam("adresse") String address) {

        return Response
                .ok(message)
                .header("Food-token", token)
                .build();
    }
}

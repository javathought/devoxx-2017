package io.github.javathought.devoxx.resources;

import io.github.javathought.devoxx.dao.TodosDao;
import io.github.javathought.devoxx.dao.UsersDao;
import io.github.javathought.devoxx.model.Todo;
import io.github.javathought.devoxx.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static io.github.javathought.devoxx.model.Role.USER;
import static io.github.javathought.devoxx.resources.CurrentUserResource.PATH;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 *
 */
@Path(PATH)
@Api(value = PATH, description = "Browse current user")
@RolesAllowed(USER)
public class CurrentUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserResource.class);

    static final String PATH = "users/current";

    @Context
    SecurityContext securityContext;


    @Produces(APPLICATION_JSON)
    @GET
    public Response getUserInformations(@Context SecurityContext sc) {
        return Response.ok().entity(sc.getUserPrincipal()).build();
    }

    @Path("current/logout")
    @Produces(APPLICATION_JSON)
    @POST
    public Response logout(@Context SecurityContext sc) {
        UsersDao.logout(sc.getUserPrincipal());
        return Response.ok().build();
    }

    @Path("/todos")
    @Produces(APPLICATION_JSON)
    @GET
    public List<Todo> getUserTodos() {
        return TodosDao.getByOwner(((User) securityContext.getUserPrincipal()).getId());

    }

    @PUT
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @ApiOperation(value = "Modify some properties of currrent user", notes = "put new password a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response update(User user) {
        if (((User) securityContext.getUserPrincipal()).getUuid() != user.getUuid()) {
            return Response.status(Response.Status.FORBIDDEN).entity("Not allowed").build();
        } else {
            UsersDao.update(user);
            return Response.ok(user).build();
        }
    }

}

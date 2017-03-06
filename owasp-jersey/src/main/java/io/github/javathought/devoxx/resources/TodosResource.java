package io.github.javathought.devoxx.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.javathought.devoxx.dao.TodosDao;
import io.github.javathought.devoxx.model.Todo;
import io.github.javathought.devoxx.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.javathought.devoxx.model.Role.USER;
import static io.github.javathought.devoxx.resources.TodosResource.PATH;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 *
 */
@Path(PATH)
@Api(value = PATH, description = "Browse Todos")
@RolesAllowed(USER)
public class TodosResource {
    public static final String PATH = "todos";

    @Inject
    SecurityContext security;

    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @ApiOperation(value = "Browse todos")
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Somethong got wrong on server")})
    public List<Todo> getTodos() {
        return TodosDao.getAll();
    }

    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @Path("/{id}")
    @ApiOperation(value = "Get todo by id", notes = "Returns todo identified by is internal id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Todo not found"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response getById(@PathParam("id") UUID id) {

        Optional<Todo> todoOptional = TodosDao.getByUuidAndUser(id, (User) security.getUserPrincipal());

        if (todoOptional.isPresent()) {
            return Response.ok().entity(todoOptional.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @ApiOperation(value = "Create a todo", notes = "post a new todo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response create(Todo todo) {
        todo.setUserId(((User)security.getUserPrincipal()).getId());
        TodosDao.create(todo);
        return Response.created(URI.create(PATH + "/" + todo.getId())).entity(todo).build();
    }

    // Resource exposed to CSRF with text plain media type
    @POST
    @Consumes({TEXT_PLAIN})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @ApiOperation(value = "Create a todo", notes = "post a new todo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response createFromText(String todoText) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Todo todo = mapper.readValue(todoText, Todo.class);
        todo.setUserId(((User)security.getUserPrincipal()).getId());
        TodosDao.create(todo);
        return Response.created(URI.create(PATH + "/" + todo.getId())).entity(todo).build();
    }

    @PUT
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    @Path("/{id}")
    @ApiOperation(value = "Modify some properties of a todo", notes = "post a new todo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response update(@PathParam("id") UUID id, Todo todo) {
        if ( !TodosDao.getByUuidAndUser(id, (User) security.getUserPrincipal()).isPresent()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            TodosDao.update(todo);
            return Response.ok(todo).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Modify some properties of a todo", notes = "post a new todo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response delete(@PathParam("id") UUID id) {
        TodosDao.delete(id);
        return Response.noContent().build();
    }

}

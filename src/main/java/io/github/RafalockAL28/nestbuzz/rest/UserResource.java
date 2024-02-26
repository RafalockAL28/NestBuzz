package io.github.RafalockAL28.nestbuzz.rest;
import io.github.RafalockAL28.nestbuzz.domain.model.User;
import io.github.RafalockAL28.nestbuzz.domain.repository.UserRepository;
import io.github.RafalockAL28.nestbuzz.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository repository;

    public UserResource(UserRepository repository){
        this.repository = repository;
    }


    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        if (userRequest == null || userRequest.getName() == null || userRequest.getAge() == null) {
            return  Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user); // Salva a entidade no banco de dados

        return Response.status(Response.Status.fromStatusCode(201).getStatusCode()).entity(user).build();
    }



    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return  Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam(("id")) Long id){
        User user = repository.findById(id);

        if(user != null){
            repository.delete(user);
            return  Response.status(Response.Status.NO_CONTENT).build();
        }
        return  Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam(("id")) Long id, CreateUserRequest userData){
        if (userData == null || userData.getName() == null || userData.getAge() == null) {
            return  Response.status(Response.Status.BAD_REQUEST).build();
        }
        User user = repository.findById(id);

        if(user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return  Response.ok().build();
        }
        return  Response.status(Response.Status.NOT_FOUND).build();

    }

}

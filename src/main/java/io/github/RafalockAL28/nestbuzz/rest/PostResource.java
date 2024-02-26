package io.github.RafalockAL28.nestbuzz.rest;


import io.github.RafalockAL28.nestbuzz.domain.model.Post;
import io.github.RafalockAL28.nestbuzz.domain.model.User;
import io.github.RafalockAL28.nestbuzz.domain.repository.PostRepository;
import io.github.RafalockAL28.nestbuzz.domain.repository.UserRepository;
import io.github.RafalockAL28.nestbuzz.rest.dto.CreatePostRequest;
import io.github.RafalockAL28.nestbuzz.rest.dto.PostResponse;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository repository;


    @Inject
    public PostResource(UserRepository userRepository, PostRepository repository){
        this.userRepository = userRepository;
        this.repository = repository;
    }


    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request){
        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setDateTime(LocalDateTime.now());
        post.setUser(user);

        repository.persist(post);
        return Response.status(Response.Status.fromStatusCode(201).getStatusCode()).entity(user).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId){

        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var query = repository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);

        var list = query.list();

        var postResponseList = list.stream().map(post -> PostResponse.fromEntity(post)).collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }

}

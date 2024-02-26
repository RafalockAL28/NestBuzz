package io.github.RafalockAL28.nestbuzz.rest;


import io.github.RafalockAL28.nestbuzz.domain.model.Follower;
import io.github.RafalockAL28.nestbuzz.domain.repository.FollowerRepository;
import io.github.RafalockAL28.nestbuzz.domain.repository.UserRepository;
import io.github.RafalockAL28.nestbuzz.rest.dto.FollowerRequest;
import io.github.RafalockAL28.nestbuzz.rest.dto.FollowerResponse;
import io.github.RafalockAL28.nestbuzz.rest.dto.FollowersPerUSerResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(FollowerResource.class.getName());


    @Inject
    public FollowerResource(FollowerRepository repository, UserRepository userRepository)  {
        this.followerRepository = repository;
        this.userRepository = userRepository;
    }


    //Follow an user
    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request){

        var user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var followerId = request.getFollowerId();


        // Impedir que o usuário siga a si mesmo
        if (userId.equals(followerId)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("User can't follow itself.")
                    .build();
        }

        var follower = userRepository.findById(request.getFollowerId());

        boolean follows = followerRepository.follows(user, follower);
        LOGGER.info("User follows target user: " + follows);

        if (!follows) {
            LOGGER.info("Creating new follower entry in database.");
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            followerRepository.persist(entity);

        }


        return Response.status(Response.Status.fromStatusCode(204)).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        // Verifica se o usuário existe
        var user = userRepository.findById(userId);
        if(user == null){
            System.out.println("User with ID " + userId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = followerRepository.findByUser(userId);

        FollowersPerUSerResponse responseObject = new FollowersPerUSerResponse();
        responseObject.setFollowersCount(list.size());

        var followersList = list.stream().map(FollowerResponse::new).collect((Collectors.toList()));
        responseObject.setContent(followersList);


        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        var user = userRepository.findById(userId);
        if (user == null) {
            System.out.println("User with ID " + userId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);


        return Response.status(Response.Status.NO_CONTENT).build();
    }



}

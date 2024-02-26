package io.github.RafalockAL28.nestbuzz.domain.repository;

import io.github.RafalockAL28.nestbuzz.domain.model.Follower;
import io.github.RafalockAL28.nestbuzz.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import static io.quarkus.arc.impl.UncaughtExceptions.LOGGER;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {


    //Verifica se um usuario já está seguindo o outro
    public boolean follows(User user, User follower) {
        Long userId = user.getId();
        Long followerId = follower.getId();

        LOGGER.info("Checking if follower " + followerId + " follows user " + userId);
        boolean result = find("FROM Follower f WHERE f.user.id = ?1 AND f.follower.id = ?2", userId, followerId).count() > 0;
        LOGGER.info("Follows result: " + result);

        return result;
    }

    public List<Follower> findByUser(Long userId) {

        System.out.println("Method findByUser started for user with ID: " + userId);
        PanacheQuery<Follower> query = find("user.id = ?1", userId);
        List<Follower> followers = query.list();
        System.out.println("Number of followers found for user with ID " + userId + ": " + followers.size());
        return followers;
    }


    public void deleteByFollowerAndUser(Long followerId, Long userId) {

        var params = Parameters
                .with("userId",userId)
                .and("followerId",followerId)
                .map();
        //follower.id da classe follower recebe o follower id daqui de cima
        delete("follower.id =:followerId and user.id =:userId",params);

    }
}

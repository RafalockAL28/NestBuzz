package io.github.RafalockAL28.nestbuzz.domain.repository;

import io.github.RafalockAL28.nestbuzz.domain.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {

}

package io.github.RafalockAL28.nestbuzz.domain.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "FOLLOWERS")
@Data  //Isso é um generate getter and setters
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
}



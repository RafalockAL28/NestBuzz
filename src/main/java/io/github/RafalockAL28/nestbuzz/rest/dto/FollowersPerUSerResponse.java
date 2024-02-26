package io.github.RafalockAL28.nestbuzz.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FollowersPerUSerResponse {

    private Integer followersCount;
    private List<FollowerResponse> content;
}

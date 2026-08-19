package com.skillduel.backend.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardEntry {
    private String username;
    private long voteCount;
}

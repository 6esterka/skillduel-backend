package com.skillduel.backend.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class VoteRequest {
    private UUID votedForUserId;
}

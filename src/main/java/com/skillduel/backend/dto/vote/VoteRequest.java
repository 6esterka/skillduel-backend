package com.skillduel.backend.dto.vote;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class VoteRequest {
    @NotNull
    private UUID votedForUserId;
}

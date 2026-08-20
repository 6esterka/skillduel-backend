package com.skillduel.backend.dto;

import com.skillduel.backend.model.Difficulty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDuelRequest {
    @NotNull
    private Difficulty difficulty;
}

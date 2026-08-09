package com.skillduel.backend.dto;

import com.skillduel.backend.model.Difficulty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDuelRequest {
    private Difficulty difficulty;
}

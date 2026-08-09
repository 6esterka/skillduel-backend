package com.skillduel.backend.dto;

import com.skillduel.backend.model.DuelStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DuelResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private DuelStatus duelStatus;
    private UUID taskId;
}

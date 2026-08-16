package com.skillduel.backend.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CodeUpdateMessage {
    private String code;
    private UUID duelId;
}

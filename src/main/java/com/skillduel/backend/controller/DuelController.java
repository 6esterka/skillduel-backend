package com.skillduel.backend.controller;

import com.skillduel.backend.dto.CreateDuelRequest;
import com.skillduel.backend.dto.DuelResponse;
import com.skillduel.backend.model.DuelStatus;
import com.skillduel.backend.model.User;
import com.skillduel.backend.service.DuelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/duels")
public class DuelController {
    private final DuelService duelService;

    public DuelController(DuelService duelService) {
        this.duelService = duelService;
    }

    @PostMapping
    public ResponseEntity<DuelResponse> createDuel(@Valid @RequestBody CreateDuelRequest createDuelRequest, @AuthenticationPrincipal User currentUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(duelService.createDuel(createDuelRequest,currentUser));
    }

    @PostMapping("/{duelId}/join")
    public ResponseEntity<DuelResponse> joinDuel(@PathVariable UUID duelId,@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(duelService.joinDuel(duelId,currentUser));
    }

    @GetMapping("/{duelId}")
    public ResponseEntity<DuelResponse> getDuel(@PathVariable UUID duelId){
        return ResponseEntity.ok(duelService.getDuel(duelId));
    }

    @GetMapping
    public ResponseEntity<List<DuelResponse>> getDuelsByStatus(@RequestParam DuelStatus duelStatus){
        return ResponseEntity.ok(duelService.getDuelsByStatus(duelStatus));
    }
    @PostMapping("/{duelId}/spectate")
    public ResponseEntity<DuelResponse> joinDuelAsSpectator(@PathVariable UUID duelId,@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(duelService.joinDuelAsSpectator(duelId,currentUser));
    }

}

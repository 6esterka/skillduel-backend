package com.skillduel.backend.controller;

import com.skillduel.backend.dto.vote.LeaderboardEntry;
import com.skillduel.backend.dto.vote.VoteRequest;
import com.skillduel.backend.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/duels/{duelId}/vote")
    public ResponseEntity<Void> submitVote(@RequestBody VoteRequest voteRequest, @PathVariable UUID duelId, Principal principal){
        voteService.submitVote(duelId,voteRequest.getVotedForUserId(),principal.getName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(){
        return ResponseEntity.ok(voteService.getLeaderboard());
    }
}

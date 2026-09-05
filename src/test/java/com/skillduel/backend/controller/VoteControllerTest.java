package com.skillduel.backend.controller;

import com.skillduel.backend.exception.ErrorMessages;
import com.skillduel.backend.exception.ResourceNotFoundException;
import com.skillduel.backend.model.Difficulty;
import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.DuelStatus;
import com.skillduel.backend.model.Task;
import com.skillduel.backend.repository.DuelRepository;
import com.skillduel.backend.repository.TaskRepository;
import com.skillduel.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends BaseControllerTest {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    DuelRepository duelRepository;
    @Autowired
    UserRepository userRepository;
    private String firstToken;
    private String secondToken;
    private String spectatorToken;
    private String duelId;
    private String firstUserId;

    @BeforeEach
    void setUp() throws Exception {
        Task task = new Task();
        task.setDescription("Find two numbers that add up to target");
        task.setDifficulty(Difficulty.EASY);
        taskRepository.save(task);
        firstToken = registerAndGetToken("dueluser", "dueluser@test.com");
        secondToken = registerAndGetToken("dueluser2", "dueluser2@test.com");
        spectatorToken = registerAndGetToken("spectator", "spectator@test.com");
        firstUserId = userRepository.findByEmail("dueluser@test.com").orElseThrow().getId().toString();
        duelId = createDuelAndGetId(firstToken);
        mockMvc.perform(post("/api/duels/" + duelId + "/join").header("Authorization", "Bearer " + secondToken));
        mockMvc.perform(post("/api/duels/" + duelId + "/spectate").header("Authorization", "Bearer " + spectatorToken));
        Duel duel = duelRepository.findById(UUID.fromString(duelId)).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NO_DUEL_FOUND));
        duel.setDuelStatus(DuelStatus.FINISHED);
        duelRepository.save(duel);
    }

    @Test
    void submitVote_shouldReturn200WhenSpectatorVotes() throws Exception {
        mockMvc.perform(post("/api/duels/" + duelId + "/vote")
                .header("Authorization", "Bearer " + spectatorToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "votedForUserId": "%s" }
                        """.formatted(firstUserId))).andExpect(status().isOk());
    }

    @Test
    void submitVote_shouldReturn400WhenVotingTwice() throws Exception {
        mockMvc.perform(post("/api/duels/" + duelId + "/vote")
                .header("Authorization", "Bearer " + spectatorToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "votedForUserId": "%s" }
                        """.formatted(firstUserId)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/duels/" + duelId + "/vote")
                        .header("Authorization", "Bearer " + spectatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "votedForUserId": "%s" }
                                """.formatted(firstUserId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void submitVote_shouldReturn400WhenNonSpectatorVotes() throws Exception {
        mockMvc.perform(post("/api/duels/" + duelId + "/vote")
                        .header("Authorization", "Bearer " + secondToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "votedForUserId": "%s" }
                                """.formatted(firstUserId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLeaderboard_shouldReturnUserAfterVote() throws Exception {
        mockMvc.perform(post("/api/duels/" + duelId + "/vote")
                        .header("Authorization", "Bearer " + spectatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "votedForUserId": "%s" }
                                """.formatted(firstUserId)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/leaderboard")
                .header("Authorization", "Bearer " + spectatorToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("dueluser"));
    }


}

package com.skillduel.backend.controller;

import com.skillduel.backend.model.Difficulty;
import com.skillduel.backend.model.Task;
import com.skillduel.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DuelControllerTest extends BaseControllerTest {
    @Autowired
    TaskRepository taskRepository;
    private String firstToken;
    private String secondToken;
    @BeforeEach
    void setUp() throws Exception{
        Task task=new Task();
        task.setDescription("Find two numbers that add up to target");
        task.setDifficulty(Difficulty.EASY);
        taskRepository.save(task);
        firstToken = registerAndGetToken("dueluser", "dueluser@test.com");
        secondToken = registerAndGetToken("dueluser2", "dueluser2@test.com");
    }

    @Test
    void getDuelsByStatus_shouldReturnDuelsWithMatchingStatus()throws Exception{
        createDuelAndGetId(firstToken);
        mockMvc.perform(get("/api/duels")
                        .header("Authorization", "Bearer " + firstToken)
                .param("duelStatus","WAITING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].duelStatus").value("WAITING"));
    }
    @Test
    void joinDuel_shouldReturn200AndStatusBecomesActive()throws Exception{
        String duelId=createDuelAndGetId(firstToken);
        mockMvc.perform(post("/api/duels/"+duelId+"/join")
                .header("Authorization", "Bearer " + secondToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duelStatus").value("ACTIVE"));
    }
    @Test
    void joinDuel_shouldReturn404WhenDuelNotFound()throws Exception{
        mockMvc.perform(post("/api/duels/"+ java.util.UUID.randomUUID() +"/join")
                        .header("Authorization", "Bearer " + firstToken))
                .andExpect(status().isNotFound());
    }
    @Test
    void joinDuel_shouldReturn400WhenDuelAlreadyActive()throws Exception{
        String duelId=createDuelAndGetId(firstToken);
        String thirdToken=registerAndGetToken("dueluser3", "dueluser3@test.com");
        mockMvc.perform(post("/api/duels/"+duelId+"/join")
                        .header("Authorization", "Bearer " + secondToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duelStatus").value("ACTIVE"));
        mockMvc.perform(post("/api/duels/"+duelId+"/join")
                .header("Authorization", "Bearer " + thirdToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDuel_shouldReturn200()throws Exception{
        String duelId=createDuelAndGetId(firstToken);
        mockMvc.perform(get("/api/duels/"+duelId)
                        .header("Authorization", "Bearer " + firstToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(duelId));
    }

    @Test
    void getDuel_shouldReturn404WhenDuelNotFound()throws Exception{
        createDuelAndGetId(firstToken);
        mockMvc.perform(get("/api/duels/"+java.util.UUID.randomUUID())
                        .header("Authorization", "Bearer " + firstToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDuel_shouldReturn403WhenNoToken()throws Exception{
        mockMvc.perform(post("/api/duels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                { "difficulty": "EASY" }
                                """
                        )
                )
                .andExpect(status().isForbidden());
    }
}

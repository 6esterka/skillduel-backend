package com.skillduel.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    protected String registerAndGetToken(String username, String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                      {
                          "username": "%s",
                          "email": "%s",
                          "password": "secret123"
                      }
                      """.formatted(username, email)))
                .andReturn();
        String body = result.getResponse().getContentAsString();
        return new ObjectMapper().readTree(body).get("token").asText();
    }

    protected String createDuelAndGetId(String token) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/duels")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                        """
                        { "difficulty": "EASY" }
                        """
                        )
                )
                .andExpect(status().isCreated())
                .andReturn();

        return new ObjectMapper()
                .readTree(result.getResponse().getContentAsString())
                .get("id").asText();
    }
}

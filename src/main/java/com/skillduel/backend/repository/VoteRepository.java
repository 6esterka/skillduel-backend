package com.skillduel.backend.repository;

import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.User;
import com.skillduel.backend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    boolean existsByDuelAndVoter(Duel duel, User voter);
}

package com.skillduel.backend.repository;

import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.DuelParticipant;
import com.skillduel.backend.model.ParticipantRole;
import com.skillduel.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DuelParticipantRepository extends JpaRepository<DuelParticipant, UUID> {
    List<DuelParticipant> findByDuel(Duel duel);
    boolean existsByDuelAndUser(Duel duel, User user);
    boolean existsByDuelAndUserAndRole(Duel duel, User user, ParticipantRole role);
}

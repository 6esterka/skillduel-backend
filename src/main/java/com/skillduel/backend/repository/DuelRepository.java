package com.skillduel.backend.repository;

import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.DuelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DuelRepository extends JpaRepository<Duel, UUID> {
   List<Duel> findByDuelStatus(DuelStatus duelStatus);
}

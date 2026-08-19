package com.skillduel.backend.repository;

import com.skillduel.backend.dto.vote.LeaderboardEntry;
import com.skillduel.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    @Query("SELECT new com.skillduel.backend.dto.vote.LeaderboardEntry(u.username, COUNT(v)) " +
            "FROM User u JOIN Vote v ON v.votedFor = u " +
            "GROUP BY u " +
            "ORDER BY COUNT(v) DESC")
    List<LeaderboardEntry> findLeaderboard();
}

package com.skillduel.backend.repository;

import com.skillduel.backend.model.Difficulty;
import com.skillduel.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDifficulty(Difficulty difficulty);
}

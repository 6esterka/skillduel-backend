package com.skillduel.backend.service;

import com.skillduel.backend.dto.CreateDuelRequest;
import com.skillduel.backend.dto.DuelResponse;
import com.skillduel.backend.exception.BusinessException;
import com.skillduel.backend.exception.ErrorMessages;
import com.skillduel.backend.exception.ResourceNotFoundException;
import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.DuelParticipant;
import com.skillduel.backend.model.DuelStatus;
import com.skillduel.backend.model.ParticipantRole;
import com.skillduel.backend.model.Task;
import com.skillduel.backend.model.User;
import com.skillduel.backend.repository.DuelParticipantRepository;
import com.skillduel.backend.repository.DuelRepository;
import com.skillduel.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class DuelService {
    private final DuelRepository duelRepository;
    private final DuelParticipantRepository duelParticipantRepository;
    private final TaskRepository taskRepository;

    public DuelService(DuelRepository duelRepository, DuelParticipantRepository duelParticipantRepository, TaskRepository taskRepository) {
        this.duelRepository = duelRepository;
        this.duelParticipantRepository = duelParticipantRepository;
        this.taskRepository = taskRepository;
    }

    public DuelResponse createDuel(CreateDuelRequest createDuelRequest, User currentUser){
        Task task=this.getRandomTask(createDuelRequest);
        Duel createdDuel=this.saveDuel(task);
        this.saveDuelParticipantByRole(createdDuel,currentUser,ParticipantRole.PLAYER);
        return new DuelResponse(createdDuel.getId(),createdDuel.getCreatedAt(),createdDuel.getDuelStatus(),createdDuel.getTask().getId());
    }

    private Task getRandomTask(CreateDuelRequest createDuelRequest){
        List<Task> tasks=taskRepository.findByDifficulty(createDuelRequest.getDifficulty());
        if(tasks.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.NO_TASKS_FOUND_BY_DIFFICULTY);
        }
        return tasks.get(new Random().nextInt(tasks.size()));
    }

    private Duel saveDuel(Task task){
        Duel newDuel=new Duel();
        newDuel.setTask(task);
        newDuel.setDuelStatus(DuelStatus.WAITING);
        return duelRepository.save(newDuel);
    }

    private void saveDuelParticipantByRole(Duel duel,User currentUser,ParticipantRole role){
        DuelParticipant duelParticipant=new DuelParticipant();
        duelParticipant.setDuel(duel);
        duelParticipant.setUser(currentUser);
        duelParticipant.setRole(role);
        duelParticipantRepository.save(duelParticipant);
    }

    public DuelResponse joinDuelAsSpectator(UUID duelId,User currentUser){
        Duel duel=duelRepository.findById(duelId).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_DUEL_FOUND));
        if(duel.getDuelStatus()!=DuelStatus.ACTIVE){
            throw new BusinessException(ErrorMessages.DUEL_NOT_STARTED);
        }
        if(duelParticipantRepository.existsByDuelAndUser(duel,currentUser)){
            throw new BusinessException(ErrorMessages.USER_ALREADY_IN_DUEL);
        }
        this.saveDuelParticipantByRole(duel,currentUser,ParticipantRole.SPECTATOR);
        return new DuelResponse(duel.getId(),duel.getCreatedAt(),duel.getDuelStatus(),duel.getTask().getId());
    }

    public DuelResponse joinDuel(UUID duelId,User currentUser){
        Duel duel=duelRepository.findById(duelId).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_DUEL_FOUND));
        if(duel.getDuelStatus()!=DuelStatus.WAITING){
            throw new BusinessException(ErrorMessages.DUEL_ALREADY_STARTED);
        }
        if(duelParticipantRepository.existsByDuelAndUser(duel,currentUser)){
            throw new BusinessException(ErrorMessages.USER_ALREADY_IN_DUEL);
        }
        this.saveDuelParticipantByRole(duel,currentUser,ParticipantRole.PLAYER);
        duel.setDuelStatus(DuelStatus.ACTIVE);
        duelRepository.save(duel);
        return new DuelResponse(duel.getId(),duel.getCreatedAt(),duel.getDuelStatus(),duel.getTask().getId());
    }

    public DuelResponse getDuel(UUID duelId){
        Duel duel=duelRepository.findById(duelId).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_DUEL_FOUND));
        return new DuelResponse(duel.getId(),duel.getCreatedAt(),duel.getDuelStatus(),duel.getTask().getId());
    }

    public List<DuelResponse> getDuelsByStatus(DuelStatus status){
        List<Duel> duels=duelRepository.findByDuelStatus(status);
        return duels.stream()
                .map(duel->new DuelResponse(duel.getId(),duel.getCreatedAt(),duel.getDuelStatus(),duel.getTask().getId()))
                .toList();
    }
}

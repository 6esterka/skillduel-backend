package com.skillduel.backend.service;

import com.skillduel.backend.dto.vote.LeaderboardEntry;
import com.skillduel.backend.exception.BusinessException;
import com.skillduel.backend.exception.ErrorMessages;
import com.skillduel.backend.exception.ResourceNotFoundException;
import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.DuelStatus;
import com.skillduel.backend.model.ParticipantRole;
import com.skillduel.backend.model.User;
import com.skillduel.backend.model.Vote;
import com.skillduel.backend.repository.DuelParticipantRepository;
import com.skillduel.backend.repository.DuelRepository;
import com.skillduel.backend.repository.UserRepository;
import com.skillduel.backend.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final DuelRepository duelRepository;
    private final DuelParticipantRepository duelParticipantRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, DuelRepository duelRepository, DuelParticipantRepository duelParticipantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.duelRepository = duelRepository;
        this.duelParticipantRepository = duelParticipantRepository;
    }

    public void submitVote(UUID duelId, UUID votedForUserId, String voterEmail){
        Duel duel=this.findDuel(duelId);
        User voter=this.findVoter(voterEmail,duel);
        User votedFor=userRepository.findById(votedForUserId).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        this.createVote(voter,duel,votedFor);
    }

    public List<LeaderboardEntry> getLeaderboard(){
        return userRepository.findLeaderboard();
    }

    private Duel findDuel(UUID duelId){
        Duel duel=duelRepository.findById(duelId).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_DUEL_FOUND));
        if(!duel.getDuelStatus().equals(DuelStatus.FINISHED)){
            throw new BusinessException(ErrorMessages.DUEL_NOT_FINISHED);
        }
        return duel;
    }

    private User findVoter(String voterEmail,Duel duel){
        User voter=userRepository.findByEmail(voterEmail).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        if(voteRepository.existsByDuelAndVoter(duel,voter)){
            throw new BusinessException(ErrorMessages.ALREADY_VOTED);
        }
        if(!duelParticipantRepository.existsByDuelAndUserAndRole(duel,voter, ParticipantRole.SPECTATOR)){
            throw new BusinessException(ErrorMessages.ONLY_SPECTATORS_CAN_VOTE);
        }
        return voter;
    }

    private void createVote(User voter,Duel duel,User votedFor){
        Vote vote=new Vote();
        vote.setVotedFor(votedFor);
        vote.setDuel(duel);
        vote.setVoter(voter);
        voteRepository.save(vote);
    }
}

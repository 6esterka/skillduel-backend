package com.skillduel.backend.service;

import com.skillduel.backend.dto.vote.LeaderboardEntry;
import com.skillduel.backend.exception.ErrorMessages;
import com.skillduel.backend.model.Duel;
import com.skillduel.backend.model.DuelStatus;
import com.skillduel.backend.model.User;
import com.skillduel.backend.model.Vote;
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

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, DuelRepository duelRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.duelRepository = duelRepository;
    }

    public void submitVote(UUID duelId, UUID votedForUserId, String voterEmail){
        Duel duel=this.findDuel(duelId);
        User voter=this.findVoter(voterEmail,duel);
        User votedFor=userRepository.findById(votedForUserId).orElseThrow(()->new RuntimeException(ErrorMessages.USER_NOT_FOUND));
        this.createVote(voter,duel,votedFor);
    }

    public List<LeaderboardEntry> getLeaderboard(){
        return userRepository.findLeaderboard();
    }

    private Duel findDuel(UUID duelId){
        Duel duel=duelRepository.findById(duelId).orElseThrow(()->new RuntimeException(ErrorMessages.NO_DUEL_FOUND));
        if(!duel.getDuelStatus().equals(DuelStatus.FINISHED)){
            throw new RuntimeException(ErrorMessages.DUEL_NOT_FINISHED);
        }
        return duel;
    }

    private User findVoter(String voterEmail,Duel duel){
        User voter=userRepository.findByEmail(voterEmail).orElseThrow(()->new RuntimeException(ErrorMessages.USER_NOT_FOUND));
        if(voteRepository.existsByDuelAndVoter(duel,voter)){
            throw new RuntimeException(ErrorMessages.ALREADY_VOTED);
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

package com.skillduel.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="votes", uniqueConstraints = @UniqueConstraint(columnNames = {"duel_id","voter_id"}))
@Getter
@Setter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name="duel_id")
    private Duel duel;
    @ManyToOne
    @JoinColumn(name="voter_id")
    private User voter;
    @ManyToOne
    @JoinColumn(name="voted_for_id")
    private User votedFor;
}

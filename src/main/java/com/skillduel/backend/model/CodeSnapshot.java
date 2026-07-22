package com.skillduel.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="code_snapshots")
@Getter
@Setter
public class CodeSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String codeResult;
    @ManyToOne
    @JoinColumn(name="code_owner_id")
    private User codeOwner;
    @ManyToOne
    @JoinColumn(name="duel_id")
    private Duel duel;
    private LocalDateTime savedAt;

    @PrePersist
    protected void onCreate(){
        this.savedAt=LocalDateTime.now();
    }
}

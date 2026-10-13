package com.chaitanya.agenticsdlc.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID requirementId;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(nullable = false, length = 100)
    private String actor;

    @Column(nullable = false, length = 3000)
    private String details;

    @Column(nullable = false)
    private Instant createdAt;

    protected AuditLog() {
    }

    public AuditLog(UUID requirementId, String action, String actor, String details) {
        this.requirementId = requirementId;
        this.action = action;
        this.actor = actor;
        this.details = details;
        this.createdAt = Instant.now();
    }
}
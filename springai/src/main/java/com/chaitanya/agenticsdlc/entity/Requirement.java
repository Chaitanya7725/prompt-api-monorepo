package com.chaitanya.agenticsdlc.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "requirements")
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequirementStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    protected Requirement() {
    }

    public Requirement(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = RequirementStatus.PENDING_ANALYSIS;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public RequirementStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void markAnalyzed() {
        this.status = RequirementStatus.ANALYZED;
    }

    public void approve() {
        this.status = RequirementStatus.APPROVED;
    }

    public void reject() {
        this.status = RequirementStatus.REJECTED;
    }
}
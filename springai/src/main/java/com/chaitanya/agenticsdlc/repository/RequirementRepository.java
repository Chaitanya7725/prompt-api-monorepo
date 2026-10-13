package com.chaitanya.agenticsdlc.repository;

import com.chaitanya.agenticsdlc.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequirementRepository extends JpaRepository<Requirement, UUID> {
}
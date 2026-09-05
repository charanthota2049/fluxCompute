package com.fluxcompute.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fluxcompute.backend.models.ResourceNode;

public interface ResourceNodeRepo extends JpaRepository<ResourceNode,Long> {
    
}

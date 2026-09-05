package com.fluxcompute.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fluxcompute.backend.models.User;
import com.fluxcompute.backend.models.Workload;

public interface WorkloadRepo extends JpaRepository<Workload, Long>{

    List<Workload> findBySubmittedBy(User user);
    
}

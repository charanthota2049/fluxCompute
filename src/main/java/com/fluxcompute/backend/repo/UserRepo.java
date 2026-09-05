package com.fluxcompute.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fluxcompute.backend.models.User;

public interface UserRepo extends JpaRepository<User,Long>{
    User findByUsername(String username);
}

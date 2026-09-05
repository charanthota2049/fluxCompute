package com.fluxcompute.backend.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fluxcompute.backend.models.User;
import com.fluxcompute.backend.repo.UserRepo;

@Service
public class UserService {
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final AuthenticationManager authManager;
    public UserService(UserRepo userRepo,JwtService jwtService,AuthenticationManager authManager, AuthenticationProvider authenticationProvider){
        this.userRepo = userRepo;
        this.authManager= authManager;
        this.jwtService = jwtService;
        this.authenticationProvider = authenticationProvider;
    }

    public void save(User user){
        userRepo.save(user);
    }

    public String verify(User user){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return "failure";
    }
}

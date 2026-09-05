package com.fluxcompute.backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fluxcompute.backend.models.MyUserDetails;
import com.fluxcompute.backend.models.User;
import com.fluxcompute.backend.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

    private final UserRepo userRepo;
    public MyUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }
    
}

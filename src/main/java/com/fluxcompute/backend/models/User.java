package com.fluxcompute.backend.models;
import com.fluxcompute.backend.enums.UserRole;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    @Column(unique = true,nullable = false)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}

package com.fluxcompute.backend.models;

import java.time.LocalDateTime;

import com.fluxcompute.backend.enums.WorkloadStatus;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workloads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Workload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String fileName;

    private String filePath;

    private Long fileSize;

    @Enumerated(EnumType.STRING)
    private WorkloadStatus status;

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User submittedBy;
}

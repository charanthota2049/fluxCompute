package com.fluxcompute.backend.models;

import java.time.LocalDateTime;

import com.fluxcompute.backend.enums.NodeStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nodeName;
    private String hostAddress;

    private Integer totalCpuCores;
    private Double cpuUsage;

    private Long totalStorage;
    private Long availableStorage;

    @Enumerated(EnumType.STRING)
    private NodeStatus status;

    private LocalDateTime lastHeartbeat;
}

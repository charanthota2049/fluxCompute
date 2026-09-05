package com.fluxcompute.backend.dto;

import java.time.LocalDateTime;

import com.fluxcompute.backend.enums.WorkloadStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadResponse {
     private Long id;

    private String name;

    private String fileName;

    private Long fileSize;

    private WorkloadStatus status;

    private LocalDateTime submittedAt;
}

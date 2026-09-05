package com.fluxcompute.backend.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fluxcompute.backend.dto.WorkloadResponse;
import com.fluxcompute.backend.services.WorkloadService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/workloads")
@RequiredArgsConstructor
public class WorkloadController {

    private final WorkloadService workloadService;

    // ---------------------------------------------------------
    // 1. Upload a workload
    // ---------------------------------------------------------
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//      @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkloadResponse> uploadWorkload(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file) {

        WorkloadResponse response =
                workloadService.uploadWorkload(name, file);

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // 2. Get all workloads submitted by current user
    // ---------------------------------------------------------
    @GetMapping("/my")
    public ResponseEntity<List<WorkloadResponse>> getMyWorkloads() {

        List<WorkloadResponse> workloads =
                workloadService.getMyWorkloads();

        return ResponseEntity.ok(workloads);
    }

    // ---------------------------------------------------------
    // 3. Get a particular workload
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<WorkloadResponse> getWorkload(
            @PathVariable Long id) {

        WorkloadResponse response =
                workloadService.getWorkload(id);

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // 4. Download workload file
    // ---------------------------------------------------------
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadWorkload(
            @PathVariable Long id) {

        Resource resource =
                workloadService.downloadWorkload(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\""
                )
                .body(resource);
    }

    // ---------------------------------------------------------
    // 5. Delete workload
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkload(
            @PathVariable Long id) {

        workloadService.deleteWorkload(id);

        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------
    // 6. Admin - get all workloads
    // ---------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WorkloadResponse>> getAllWorkloads() {

        List<WorkloadResponse> workloads =
                workloadService.getMyWorkloads();

        return ResponseEntity.ok(workloads);
    }
}

package com.fluxcompute.backend.services;

import com.fluxcompute.backend.controllers.UserController;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fluxcompute.backend.dto.WorkloadResponse;
import com.fluxcompute.backend.enums.WorkloadStatus;
import com.fluxcompute.backend.models.User;
import com.fluxcompute.backend.models.Workload;
import com.fluxcompute.backend.repo.UserRepo;
import com.fluxcompute.backend.repo.WorkloadRepo;

@Service
public class WorkloadService {

    private final UserRepo userRepo;
    private final WorkloadRepo workloadRepo;
    private final Path storageLocation =
            Paths.get("storage/workloads");
    
    public WorkloadService(UserRepo userRepo,WorkloadRepo workloadRepo){
        this.userRepo = userRepo;
        this.workloadRepo = workloadRepo;
    }

    public WorkloadResponse uploadWorkload(String name, MultipartFile file) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepo.findByUsername(username);
            if(user==null){
                throw new RuntimeException("User not found");
            }

            Workload workload = new Workload();
            workload.setName(name);
            workload.setFileName(file.getOriginalFilename());
            workload.setFileSize(file.getSize());
            workload.setStatus(WorkloadStatus.UPLOADED);
            workload.setSubmittedAt(
                    java.time.LocalDateTime.now()
            );
            workload.setSubmittedBy(user);

            Workload savedWorkload = workloadRepo.save(workload);
            
            Path workloadDirectory =
                    storageLocation.resolve(
                            String.valueOf(savedWorkload.getId())
                    );
            Files.createDirectories(workloadDirectory);

            String originalFileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID()+"_"+originalFileName;

            Path filePath = workloadDirectory.resolve(storedFileName);
                        Files.copy(
                    file.getInputStream(),
                    filePath
            );

            savedWorkload.setFilePath(
                    filePath.toString()
            );

            savedWorkload =
                    workloadRepo.save(savedWorkload);

            return convertToResponse(savedWorkload);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{

        }
        return new WorkloadResponse();
    }

    public List<WorkloadResponse> getMyWorkloads() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepo.findByUsername(username);
            if(user==null){
                throw new RuntimeException("User not found");
            }
            List<Workload> workloads =
                workloadRepo.findBySubmittedBy(user);

            return workloads.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());        
    }

    public WorkloadResponse getWorkload(Long id) {
        Workload workload = workloadRepo.findById(id)
                                        .orElseThrow(
                                                () -> new RuntimeException("workload not found")
                                                    
                                            );
        return convertToResponse(workload);
    }

    // ---------------------------------------------------------
    // Download workload file
    // ---------------------------------------------------------
    public Resource downloadWorkload(Long id) {

        Workload workload =
                workloadRepo.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Workload not found"
                                )
                        );

        Path filePath =
                Paths.get(workload.getFilePath());

        Resource resource =
                new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new RuntimeException(
                    "Workload file not found"
            );
        }

        return resource;
    }

    // ---------------------------------------------------------
    // Delete a workload
    // ---------------------------------------------------------
    public void deleteWorkload(Long id) {

        Workload workload =
                workloadRepo.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Workload not found"
                                )
                        );

        try {

            // Delete actual file
            if (workload.getFilePath() != null) {

                Path filePath =
                        Paths.get(workload.getFilePath());

                Files.deleteIfExists(filePath);
            }

            // Delete database record
            workloadRepo.delete(workload);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete workload file",
                    e
            );
        }
    }

    // ---------------------------------------------------------
    // Convert Workload entity to WorkloadResponse DTO
    // ---------------------------------------------------------
    private WorkloadResponse convertToResponse(
            Workload workload) {

        return new WorkloadResponse(
                workload.getId(),
                workload.getName(),
                workload.getFileName(),
                workload.getFileSize(),
                workload.getStatus(),
                workload.getSubmittedAt()
        );
    }
    
}
package com.fluxcompute.backend.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/jobs")
public class JobResultController {

    @PostMapping(
                value="/{jobId}/result",
                consumes=MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadResult(
        @PathVariable Long jobId,
        @RequestParam String status,
        @RequestParam long exitCode,
        @RequestParam(defaultValue = "") String logs,
        @RequestPart("artifacts") MultipartFile artifacts
    ) throws Exception{
        System.out.println("hitting jobResultController-------------->>>>>>>>>>>>");

        if (!status.equals("COMPLETED") && !status.equals("FAILED")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid job status"));
        }

        if (artifacts.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Artifacts ZIP is empty"));
        }

        Path resultRoot = Path.of("storage","job-results")
                                .toAbsolutePath()
                                .normalize();

        Path jobDirectory = resultRoot.resolve(jobId.toString()).normalize();

        // Prevent path traversal.
        if (!jobDirectory.startsWith(resultRoot)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid job ID"));
        }

        Files.createDirectories(jobDirectory);

        Path artifactPath = jobDirectory.resolve("artifacts.zip");

        try(var input = artifacts.getInputStream()){
            Files.copy(input,
                artifactPath,
                StandardCopyOption.REPLACE_EXISTING
            );
        }

        return ResponseEntity.ok(Map.of(
            "message","job result received",
            "jobId",jobId,
            "status",status
        ));
    }
}

package com.fluxcompute.backend.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxcompute.backend.enums.NodeStatus;
import com.fluxcompute.backend.models.ResourceNode;
import com.fluxcompute.backend.services.ResourceNodeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/auth/nodes")
public class ResourceNodeController {

    private final ResourceNodeService resourceNodeService;

    public ResourceNodeController(ResourceNodeService resourceNodeService) {
        this.resourceNodeService = resourceNodeService;
    }

    // ---------------------------------------------------------
    // Get all resource nodes
    // GET /api/auth/nodes
    // ---------------------------------------------------------
    @GetMapping()
    public ResponseEntity<List<ResourceNode>> getAllResourceNodes() {
        return ResponseEntity.ok(
            resourceNodeService.getAllResourceNodes()
        );
    }

    // ---------------------------------------------------------
    // Get a resource node by its ID
    // GET /api/auth/nodes/{id}
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ResourceNode> getResourceNodeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            resourceNodeService.getResourceNodeById(id)
        );
    }

    // ---------------------------------------------------------
    // Register a new resource node
    // POST /api/auth/nodes
    // ---------------------------------------------------------
    @PostMapping()
    public ResponseEntity<ResourceNode> registerResourceNode(
            @RequestBody ResourceNode resourceNode) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resourceNodeService.save(resourceNode));
    }

    // ---------------------------------------------------------
    // Update an existing resource node
    // PUT /api/auth/nodes/{id}
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ResourceNode> updateResourceNode(
            @PathVariable Long id,
            @RequestBody ResourceNode resourceNode) {

        return ResponseEntity.ok(
            resourceNodeService.updateResourceNode(id, resourceNode)
        );
    }

    // ---------------------------------------------------------
    // Update the status of a resource node
    // PATCH /api/auth/nodes/{id}/status
    // ---------------------------------------------------------
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResourceNode> updateResourceNodeStatus(
            @PathVariable Long id,
            @RequestBody NodeStatus status) {

        return ResponseEntity.ok(
            resourceNodeService.updateStatus(id, status)
        );
    }

    // ---------------------------------------------------------
    // Delete a resource node
    // DELETE /api/auth/nodes/{id}
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceNode(
            @PathVariable Long id) {

        resourceNodeService.deleteResourceNode(id);

        return ResponseEntity.noContent().build();
    }
}
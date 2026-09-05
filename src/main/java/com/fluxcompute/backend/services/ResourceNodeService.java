package com.fluxcompute.backend.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.fluxcompute.backend.enums.NodeStatus;
import com.fluxcompute.backend.models.ResourceNode;
import com.fluxcompute.backend.repo.ResourceNodeRepo;

@Service
public class ResourceNodeService {
    private final ResourceNodeRepo resourceNodeRepo;

    public ResourceNodeService(ResourceNodeRepo resourceNodeRepo){
        this.resourceNodeRepo = resourceNodeRepo;
    }

    public ResourceNode save(ResourceNode resourceNode){
        return resourceNodeRepo.save(resourceNode);
    }

    public List<ResourceNode> getAllResourceNodes() {
        return resourceNodeRepo.findAll();
    }

    public ResourceNode getResourceNodeById(Long id) {
        Optional<ResourceNode> node = resourceNodeRepo.findById(id);
        if(node.isPresent()) return node.get();
        throw new RuntimeException("Resource not found with id: "+id);
    }

    public ResourceNode updateResourceNode(Long id, ResourceNode resourceNode) {
        Optional<ResourceNode> existingNode = resourceNodeRepo.findById(id);
        if(existingNode.isPresent()){
            ResourceNode temp = existingNode.get();
            temp.setAvailableStorage(resourceNode.getAvailableStorage());
            temp.setCpuUsage(resourceNode.getCpuUsage());
            temp.setHostAddress(resourceNode.getHostAddress());
            temp.setLastHeartbeat(resourceNode.getLastHeartbeat());
            temp.setNodeName(resourceNode.getNodeName());
            temp.setStatus(resourceNode.getStatus());
            temp.setTotalCpuCores(resourceNode.getTotalCpuCores());
            temp.setTotalStorage(resourceNode.getTotalStorage());
            return resourceNodeRepo.save(temp);
        }
        else{
            throw new RuntimeException("Resource not found with id: "+id);
        }
    }

    public void deleteResourceNode(Long id) {
        if(!resourceNodeRepo.existsById(id)){
            throw new RuntimeException("Resource not found with id: "+id);
        }
        resourceNodeRepo.deleteById(id);
    }

    public ResourceNode updateStatus(Long id, NodeStatus status) {
        Optional<ResourceNode> existingNode = resourceNodeRepo.findById(id);
        if(existingNode.isPresent()){
            ResourceNode temp = existingNode.get();
            temp.setStatus(status);
            return resourceNodeRepo.save(temp);
        }
        else{
            throw new RuntimeException("Resource not found with id: "+id);
        }
    }
}

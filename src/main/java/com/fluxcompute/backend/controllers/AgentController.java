package com.fluxcompute.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxcompute.backend.services.AgentCommunicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/test-agent")
public class AgentController {
    private final AgentCommunicationService agentCommunicationService;
    public AgentController(AgentCommunicationService agentCommunicationService){
        this.agentCommunicationService = agentCommunicationService;
    }

    @PostMapping("/send-job")
    public String postMethodName(@RequestBody String entity) {
        System.out.println("-------------");
        agentCommunicationService.sendJobToAgent(1L,2L,1L);
        return "job notification sent";
    }
    
}

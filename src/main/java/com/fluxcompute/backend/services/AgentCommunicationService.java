package com.fluxcompute.backend.services;

import com.fluxcompute.backend.services.AgentWebSocketHandler;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

@Service 
public class AgentCommunicationService {
    public final AgentWebSocketHandler agentWebSocketHandler;
    private final ObjectMapper objectMapper;

    public AgentCommunicationService(AgentWebSocketHandler agentWebSocketHandler,ObjectMapper objectMapper){
        this.agentWebSocketHandler = agentWebSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void sendJobToAgent(Long nodeId,Long jobId,Long workloadId){
        Map<String,Object> message = Map.of(
            "type","RUN_JOB",
            "jobId",jobId,
            "workloadId",workloadId
        );
        try{
            String json = objectMapper.writeValueAsString(message);
            agentWebSocketHandler.sendToAgent(nodeId, json);
            System.out.println("job sent to agent");
        }
        catch(IOException e){
            throw new IllegalStateException("could not send the job to agent ",e);
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}

package com.fluxcompute.backend.services;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component 
public class AgentWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public AgentWebSocketHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override 
    public void afterConnectionEstablished(WebSocketSession session){
        System.out.println("========== WEBSOCKET CONNECTED ==========");
        System.out.println("Session ID: " + session.getId());
        System.out.println("URI: " + session.getUri());

        Long nodeId = getNodeId(session);
        if(nodeId!=null){
            sessions.put(nodeId,session);
            System.out.println("Agent connected :"+nodeId);
            System.out.println("Registered Agent: " + nodeId);
            System.out.println("All connected agents: " + sessions.keySet());
        }
        else{
            try{
                session.close(CloseStatus.BAD_DATA);
            }
            catch(IOException e){
                System.err.println("Could not close invalid session");
            }
        }
    }

    public void sendToAgent(Long nodeId,String message)throws IOException{
        WebSocketSession session = sessions.get(nodeId);
        if(session==null || !session.isOpen()){
            throw new IllegalStateException("Agent is not Connected:"+nodeId);
        }
        synchronized(session){
            session.sendMessage(new TextMessage(message));
        }
    }

    @Override 
    public void handleTextMessage(WebSocketSession session,TextMessage message){
        JsonNode json = objectMapper.readTree(message.getPayload());
        System.out.println("-------------------------------");
        if(json.path("type").equals("RESOURCE_STATUS")){
            double cpuUsage = json.path("cpuUsage").asDouble();
            long totalMemory = json.path("totalMemory").asLong();
            long availableMemory = json.path("availableMemory").asLong();
            double memoryUsage = json.path("memoryUsage").asDouble();
            long totalStorage = json.path("totalStorage").asLong();
            long freeStorage = json.path("freeStorage").asLong();
    
            System.out.println("Received resource update:");
            System.out.println("CPU Usage: " + cpuUsage + "%");
            System.out.println("Memory Usage: " + memoryUsage + "%");
            System.out.println("Available RAM: " + availableMemory);
            System.out.println("Free Storage: " + freeStorage);
        }
    }

    
    @Override 
    public void afterConnectionClosed(WebSocketSession session,CloseStatus status){
        Long nodeId = getNodeId(session);
        if(nodeId!=null){
            sessions.remove(nodeId,session);
            System.out.println("Agent disconnected : "+nodeId);
        }
    }


    private Long getNodeId(WebSocketSession session) {
        if (session.getUri() == null) {
            return null;
        }

        String query = session.getUri().getQuery();

        System.out.println("Query string: " + query);

        if (query == null || query.isBlank()) {
            return null;
        }

        for (String param : query.split("&")) {
            String[] pair = param.split("=", 2);

            if (pair.length == 2 && pair[0].equals("nodeId")) {
                try {
                    return Long.parseLong(pair[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid nodeId: " + pair[1]);
                    return null;
                }
            }
        }

        return null;
    }
}

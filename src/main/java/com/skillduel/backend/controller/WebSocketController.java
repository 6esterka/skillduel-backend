package com.skillduel.backend.controller;

import com.skillduel.backend.dto.websocket.CodeBroadcastMessage;
import com.skillduel.backend.dto.websocket.CodeUpdateMessage;
import com.skillduel.backend.exception.ErrorMessages;
import com.skillduel.backend.model.User;
import com.skillduel.backend.repository.UserRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }
    @MessageMapping("/duel/{duelId}/code")
    public void handleCodeUpdate(@DestinationVariable UUID duelId, @Payload CodeUpdateMessage message, Principal principal){
        String username= principal.getName();
        User user=this.userRepository.findByEmail(username).orElseThrow(()->new RuntimeException(ErrorMessages.USER_NOT_FOUND));
        CodeBroadcastMessage codeBroadcastMessage=new CodeBroadcastMessage(message.getCode(),user.getId(),duelId);
        messagingTemplate.convertAndSend("/topic/duel/"+duelId,codeBroadcastMessage);
    }
}

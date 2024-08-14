package com.yarzar.chat.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.yarzar.chat.chat.ChatMessage;
import com.yarzar.chat.chat.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocetEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    
    @EventListener
    public void handleWebSocketDisconnectListener(
        SessionDisconnectEvent event
    ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("User disconnected: {}", username);
            val chatMessage = ChatMessage.builder()
                .type(MessageType.LEAVE)
                .sender(username)
                .build();
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}

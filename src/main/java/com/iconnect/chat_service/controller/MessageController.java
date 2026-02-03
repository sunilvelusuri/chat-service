package com.iconnect.chat_service.controller;


import com.iconnect.chat_service.controller.dto.ChatMessageResponse;
import com.iconnect.chat_service.controller.dto.ReadReceiptRequest;
import com.iconnect.chat_service.controller.dto.SendMessageRequest;
import com.iconnect.chat_service.controller.dto.TypingRequest;
import com.iconnect.chat_service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/rooms")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable("id") Long userId,
            @RequestBody SendMessageRequest request) {

        messageService.sendMessage(
                userId,
                request.getSenderUserId(),
                request.getContent()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable Long id,
            @RequestParam String userId,
            @RequestParam(defaultValue = "20") int limit) {

        return ResponseEntity.ok(
                messageService.getMessages(id, userId, limit)
        );
    }

    @PostMapping("/{id}/typing")
    public ResponseEntity<?> typing(
            @PathVariable Long id,
            @RequestBody TypingRequest request) {

        messageService.sendTyping(
                id,
                request.getUserId(),
                request.isTyping()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> read(
            @PathVariable Long id,
            @RequestBody ReadReceiptRequest request) {

        messageService.sendReadReceipt(
                id,
                request.getUserId(),
                request.getEventId()
        );

        return ResponseEntity.ok().build();
    }


}


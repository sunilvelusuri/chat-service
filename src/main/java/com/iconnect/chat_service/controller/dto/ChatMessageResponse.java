package com.iconnect.chat_service.controller.dto;

public class ChatMessageResponse {

    private String senderUserId;
    private String content;
    private long timestamp;

    public ChatMessageResponse(String senderUserId,
                               String content,
                               long timestamp) {
        this.senderUserId = senderUserId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderUserId() { return senderUserId; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
}


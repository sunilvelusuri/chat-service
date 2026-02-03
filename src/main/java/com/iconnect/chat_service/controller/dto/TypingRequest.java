package com.iconnect.chat_service.controller.dto;

public class TypingRequest {
    private String userId;
    private boolean typing;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isTyping() { return typing; }
    public void setTyping(boolean typing) { this.typing = typing; }
}


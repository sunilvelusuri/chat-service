package com.iconnect.chat_service.controller.dto;


public class CreateDmRequest {
    private String fromUserId;
    private String toUserId;

    public String getFromUserId() { return fromUserId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }

    public String getToUserId() { return toUserId; }
    public void setToUserId(String toUserId) { this.toUserId = toUserId; }
}


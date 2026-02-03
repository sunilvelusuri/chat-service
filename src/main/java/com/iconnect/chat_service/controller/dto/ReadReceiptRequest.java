package com.iconnect.chat_service.controller.dto;


public class ReadReceiptRequest {
    private String userId;
    private String eventId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
}


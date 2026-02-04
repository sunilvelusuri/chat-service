package com.iconnect.chat_service.controller.dto;

public class MatrixLoginResponse{
    private String user_id;
    private String access_token;
    private String device_id;
    private String home_server;

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getDevice_id() {
        return device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
    public String getHome_server() {
        return home_server;
    }
    public void setHome_server(String homeserver) {
        this.home_server = homeserver;
    }
}

package com.iconnect.chat_service.controller.dto;

import lombok.Setter;

public class MatrixLoginRequest {
    private String type = "m.login.password";
    @Setter
    private Identifier identifier;
    @Setter
    private String password;

    public static class Identifier{
        private String type = "m.id.user";
        @Setter
        private String user;
        public String getType() {
            return type;
        }
        public String getUser() {
            return user;
        }
    }

    public String getType() {
        return type;
    }
    public Identifier getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }

}

package com.iconnect.chat_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "memberships")
@Getter
@Setter
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private String iconnectUserId;
    private String role;

    public Membership() {}

    public Membership(Long roomId, String iconnectUserId, String role) {
        this.roomId = roomId;
        this.iconnectUserId = iconnectUserId;
        this.role = role;
    }

    // getters & setters
}

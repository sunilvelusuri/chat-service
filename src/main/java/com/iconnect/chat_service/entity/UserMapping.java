package com.iconnect.chat_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_mapping")
@Getter
@Setter
public class UserMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String iconnectUserId;

    @Column(unique = true, nullable = false)
    private String matrixUserId;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
}


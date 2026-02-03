package com.iconnect.chat_service.controller;


import com.iconnect.chat_service.controller.dto.CreateDmRequest;
import com.iconnect.chat_service.entity.Room;
import com.iconnect.chat_service.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/dm")
    public ResponseEntity<Room> createDm(@RequestBody CreateDmRequest request) {

        Room room = roomService.createDm(
                request.getFromUserId(),
                request.getToUserId()
        );

        return ResponseEntity.ok(room);
    }
}


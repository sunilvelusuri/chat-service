package com.iconnect.chat_service.service;


import com.iconnect.chat_service.client.MatrixClient;
import com.iconnect.chat_service.controller.dto.ChatMessageResponse;
import com.iconnect.chat_service.entity.Room;
import com.iconnect.chat_service.entity.UserMapping;
import com.iconnect.chat_service.repository.MembershipRepository;
import com.iconnect.chat_service.repository.RoomRepository;
import com.iconnect.chat_service.repository.UserMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final RoomRepository roomRepo;
    private final MembershipRepository membershipRepo;
    private final UserMappingRepository userRepo;
    private final MatrixClient matrixClient;

    public MessageService(RoomRepository roomRepo,
                          MembershipRepository membershipRepo,
                          UserMappingRepository userRepo,
                          MatrixClient matrixClient) {
        this.roomRepo = roomRepo;
        this.membershipRepo = membershipRepo;
        this.userRepo = userRepo;
        this.matrixClient = matrixClient;
    }

    public void sendMessage(Long roomId,
                            String senderUserId,
                            String content) {

        // 1. Validate room
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 2. Validate membership
        boolean isMember =
                membershipRepo.existsByRoomIdAndIconnectUserId(
                        roomId, senderUserId);

        if (!isMember) {
            throw new RuntimeException("User not in room");
        }

        // 3. Resolve Matrix user
        UserMapping mapping =
                userRepo.findByIconnectUserId(senderUserId)
                        .orElseThrow(() -> new RuntimeException("User not mapped"));

        // 4. Send message via Matrix
        matrixClient.sendMessage(
                room.getMatrixRoomId(),
                mapping.getMatrixUserId(),
                content
        );
    }
    public List<ChatMessageResponse> getMessages(
            Long roomId,
            String requesterUserId,
            int limit) {

        // 1. Validate room
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 2. Validate membership
        boolean isMember =
                membershipRepo.existsByRoomIdAndIconnectUserId(
                        roomId, requesterUserId);

        if (!isMember) {
            throw new RuntimeException("User not in room");
        }

        // 3. Fetch from Matrix
        return matrixClient.fetchMessages(
                room.getMatrixRoomId(), limit);
    }


//    public void sendMessage(Long roomId,
//                            String senderUserId,
//                            String content) {
//
//        Room room = roomRepo.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        boolean isMember =
//                membershipRepo.existsByRoomIdAndUserId(
//                        roomId, senderUserId);
//
//        if (!isMember) {
//            throw new RuntimeException("User not in room");
//        }
//
//        UserMapping mapping =
//                userRepo.findByIconnectUserId(senderUserId)
//                        .orElseThrow(() -> new RuntimeException("User not mapped"));
//
//        matrixClient.sendMessage(
//                room.getMatrixRoomId(),
//                mapping.getMatrixUserId(),
//                content
//        );
//    }



    public void sendTyping(Long roomId,
                           String userId,
                           boolean typing) {

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!membershipRepo.existsByRoomIdAndIconnectUserId(roomId, userId)) {
            throw new RuntimeException("User not in room");
        }

        UserMapping mapping = userRepo.findByIconnectUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not mapped"));

        matrixClient.sendTyping(
                room.getMatrixRoomId(),
                mapping.getMatrixUserId(),
                typing
        );
    }

    public void sendReadReceipt(Long roomId,
                                String userId,
                                String eventId) {

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!membershipRepo.existsByRoomIdAndIconnectUserId(roomId, userId)) {
            throw new RuntimeException("User not in room");
        }

        UserMapping mapping = userRepo.findByIconnectUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not mapped"));

        matrixClient.sendReadReceipt(
                room.getMatrixRoomId(),
                mapping.getMatrixUserId(),
                eventId
        );
    }



}

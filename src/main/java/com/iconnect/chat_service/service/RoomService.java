package com.iconnect.chat_service.service;

import com.iconnect.chat_service.client.MatrixClient;
import com.iconnect.chat_service.entity.Membership;
import com.iconnect.chat_service.entity.Room;
import com.iconnect.chat_service.repository.MembershipRepository;
import com.iconnect.chat_service.repository.RoomRepository;
import com.iconnect.chat_service.repository.UserMappingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepo;
    private final MembershipRepository membershipRepo;
    private final MatrixClient matrixClient;
    private final UserMappingRepository userMappingRepository;

    public RoomService(RoomRepository roomRepo,
                       MembershipRepository membershipRepo,
                       MatrixClient matrixClient,
                       UserMappingRepository userMappingRepository) {
        this.roomRepo = roomRepo;
        this.membershipRepo = membershipRepo;
        this.matrixClient = matrixClient;
        this.userMappingRepository = userMappingRepository;
    }

//    @Transactional
//    public Room createDm(String fromUserId, String toUserId) {
//
//        // 1. Check existing DM
//        Optional<Room> existing = roomRepo.findExistingDm(fromUserId, toUserId);
//
//        if (existing.isPresent()) {
//            return existing.get();
//        }
//
//        // 2. Create Matrix room
//        String matrixRoomId = matrixClient.createDmRoom(fromUserId, toUserId);
//
//        // 3. Save room
//        Room room = new Room();
//        room.setMatrixRoomId(matrixRoomId);
//        room.setRoomType("DM");
//        room.setCreatedBy(fromUserId);
//        Room savedRoom = roomRepo.save(room);
//
//        // 4. Save memberships
//        membershipRepo.save(
//                new Membership(savedRoom.getId(), fromUserId, "MEMBER"));
//        membershipRepo.save(
//                new Membership(savedRoom.getId(), toUserId, "MEMBER"));
//
//        return savedRoom;
//    }

    @Transactional
    public Room createDm(String fromUserId, String toUserId) {

        // 1️⃣ Idempotency
        Optional<Room> existing = roomRepo.findExistingDm(fromUserId, toUserId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 2️⃣ Resolve Matrix IDs
        String fromMatrixUserId = userMappingRepository
                .findByIconnectUserId(fromUserId)
                .orElseThrow(() ->
                        new IllegalStateException("User not registered: " + fromUserId))
                .getMatrixUserId();

        String toMatrixUserId = userMappingRepository
                .findByIconnectUserId(toUserId)
                .orElseThrow(() ->
                        new IllegalStateException("User not registered: " + toUserId))
                .getMatrixUserId();

        // 3️⃣ Create Matrix room
        String matrixRoomId = matrixClient.createDmRoom(fromUserId, toUserId);

        // 4️⃣ Invite second user
        matrixClient.inviteUser(matrixRoomId, toMatrixUserId);

        // 5️⃣ Persist room (FIX IS HERE)
        Room room = new Room();
        room.setMatrixRoomId(matrixRoomId);
        room.setRoomType("DM");

        // 🔴 THIS LINE WAS MISSING
        room.setCreatedBy(fromUserId);

        Room savedRoom = roomRepo.save(room);

        // 6️⃣ Persist memberships
        membershipRepo.save(
                new Membership(savedRoom.getId(), fromUserId, "MEMBER"));
        membershipRepo.save(
                new Membership(savedRoom.getId(), toUserId, "MEMBER"));

        return savedRoom;
    }


}



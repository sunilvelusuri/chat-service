package com.iconnect.chat_service.repository;


import com.iconnect.chat_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
       SELECT r FROM Room r
       WHERE r.roomType = 'DM'
       AND r.id IN (
           SELECT m1.roomId FROM Membership m1
           JOIN Membership m2 ON m1.roomId = m2.roomId
           WHERE m1.iconnectUserId = :user1
             AND m2.iconnectUserId = :user2
       )
    """)
    Optional<Room> findExistingDm(String user1, String user2);

    Optional<Room> findByMatrixRoomId(String matrixRoomId);
}


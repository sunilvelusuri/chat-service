package com.iconnect.chat_service.repository;

import com.iconnect.chat_service.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository
        extends JpaRepository<Membership, Long> {

    // Find all memberships for a room
    List<Membership> findByRoomId(Long roomId);

    // Check if user already belongs to a room
    boolean existsByRoomIdAndIconnectUserId(Long roomId, String iconnectUserId);

    // Find all rooms a user belongs to
    List<Membership> findByIconnectUserId(String iconnectUserId);

//    boolean existsByRoomIdAndUserId(Long roomId, String senderUserId);
}


package com.iconnect.chat_service.repository;


import com.iconnect.chat_service.entity.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMappingRepository extends JpaRepository<UserMapping, Long> {

    Optional<UserMapping> findByIconnectUserId(String iconnectUserId);


}


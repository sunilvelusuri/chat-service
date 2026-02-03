package com.iconnect.chat_service.service;


import com.iconnect.chat_service.client.MatrixClient;
import com.iconnect.chat_service.entity.UserMapping;
import com.iconnect.chat_service.repository.UserMappingRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMappingRepository repo;
    private final MatrixClient matrixClient;

    public UserService(UserMappingRepository repo,
                       MatrixClient matrixClient) {
        this.repo = repo;
        this.matrixClient = matrixClient;
    }

    public UserMapping registerUser(String iconnectUserId) {

        return repo.findByIconnectUserId(iconnectUserId)
                .orElseGet(() -> {
                    // call matrix
                    String matrixId = matrixClient.createUser(iconnectUserId);
                    UserMapping mapping = new UserMapping();
                    mapping.setIconnectUserId(iconnectUserId);
                    mapping.setMatrixUserId(matrixId);
                    return repo.save(mapping);
                });
    }
}


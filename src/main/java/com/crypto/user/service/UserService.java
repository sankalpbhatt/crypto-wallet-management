package com.crypto.user.service;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(String id);

    @Transactional
    User updateUser(UUID id, User updatedUser);
}

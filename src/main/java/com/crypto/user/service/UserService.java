package com.crypto.user.service;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(String id);

    UserResponse getUserByInternalId(UUID id);
}

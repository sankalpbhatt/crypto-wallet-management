package com.crypto.user.service;

import com.crypto.user.dto.request.CreateUserRequest;
import com.crypto.user.dto.request.UpdateUserRequest;
import com.crypto.user.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(String id);

    UserResponse getUserByInternalId(UUID id);

    void deleteUserById(String id);

    UserResponse updateUser(String id, UpdateUserRequest updateUserRequest);
}

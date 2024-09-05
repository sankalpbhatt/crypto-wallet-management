package com.crypto.user.service;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
}

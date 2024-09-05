package com.crypto.user.service.impl;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.mapper.UserMapper;
import com.crypto.user.repository.UserRepository;
import com.crypto.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        return userMapper.mapToDto(userRepository.save(userMapper.mapToEntity(request)));
    }

    @Override
    public UserResponse getUser(String id) {
        return userMapper.mapToDto(
                        userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("User Not found")
                                )
                );
    }
}

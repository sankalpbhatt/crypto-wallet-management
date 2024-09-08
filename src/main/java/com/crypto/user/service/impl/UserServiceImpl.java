package com.crypto.user.service.impl;

import com.crypto.common.entity.SequenceType;
import com.crypto.common.service.impl.SequenceGeneratorServiceImpl;
import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.entity.User;
import com.crypto.user.mapper.UserMapper;
import com.crypto.user.repository.UserRepository;
import com.crypto.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl  extends SequenceGeneratorServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        String userId = SequenceType.USER.getPrefix() + getNextSequenceValue(SequenceType.USER);
        User user = userMapper.mapToEntity(request);
        user.setUserId(userId);
        return userMapper.mapToDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(String id) {
        User user = userRepository.findByUserId(id).orElseThrow(() -> new NoSuchElementException("User Not found"));
        return userMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public User updateUser(UUID id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }
}

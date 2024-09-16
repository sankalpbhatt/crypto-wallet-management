package com.crypto.user.service.impl;

import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.common.service.SequenceGeneratorServiceImpl;
import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import com.crypto.user.dto.request.CreateUserRequest;
import com.crypto.user.dto.request.UpdateUserRequest;
import com.crypto.user.dto.response.UserResponse;
import com.crypto.user.entity.User;
import com.crypto.user.mapper.UserMapper;
import com.crypto.user.repository.UserRepository;
import com.crypto.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl extends SequenceGeneratorServiceImpl implements UserService {

    private static final String ERROR_MESSAGE_USER_NOT_FOUND = "User Not found";
    private static final Random random = new Random();
    private static final Integer randomBound = 1501;

    private final UserRepository userRepository;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           SequenceGeneratorRepository sequenceGeneratorRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validateUserAlreadyExists(request);

        String userId = SequenceType.USER.getPrefix() + getNextSequenceValue(SequenceType.USER, sequenceGeneratorRepository);
        int hashIterations = random.nextInt(randomBound) + 500;
        User user = userMapper.mapToEntity(request, hashIterations);
        user.setUserId(userId);

        return userMapper.mapToResponseDto(userRepository.save(user));
    }

    private void validateUserAlreadyExists(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new MyServiceException("Email already exists", ErrorCode.BUSINESS_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(String id) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE_USER_NOT_FOUND));
        return userMapper.mapToResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByInternalId(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE_USER_NOT_FOUND));
        return userMapper.mapToResponseDto(user);
    }

    @Override
    public void deleteUserById(String id) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE_USER_NOT_FOUND));
        user.setDeletedDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserResponse updateUser(String id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException(ERROR_MESSAGE_USER_NOT_FOUND));
        user.setUpdatedDate(LocalDateTime.now());
        if (Objects.nonNull(updateUserRequest.getFirstName())) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (Objects.nonNull(updateUserRequest.getLastName())) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (Objects.nonNull(updateUserRequest.getPhone())) {
            user.setPhone(updateUserRequest.getPhone());
        }
        return userMapper.mapToResponseDto(userRepository.save(user));
    }
}

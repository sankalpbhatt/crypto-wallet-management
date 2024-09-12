package com.crypto.user.service.impl;

import com.crypto.common.entity.SequenceGenerator;
import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.exception.MyServiceException;
import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.entity.User;
import com.crypto.user.mapper.UserMapper;
import com.crypto.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    @Mock
    private UserMapper userMapper;

    private UserServiceImpl userService;

    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, sequenceGeneratorRepository, userMapper);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "firstName",
                "lastName",
                "1234567890",
                "test@example.com",
                "TestUser");
        User user = new User();
        user.setEmail(request.email());
        user.setUserId("U2");
        UUID userInternalId = UUID.randomUUID();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(sequenceGeneratorRepository.findBySequenceType(any()))
                .thenReturn(Optional.of(new SequenceGenerator(SequenceType.USER.name(), 1l)));
        when(userMapper.mapToEntity(any(CreateUserRequest.class), anyInt())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.mapToResponseDto(any(User.class)))
                .thenReturn(new UserResponse("U2",
                        userInternalId,
                        "firstName",
                        "lastName",
                        "2134567890",
                        "test@example.com"));

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("U2");
        assertThat(response.email()).isEqualTo("test@example.com");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUserId()).isEqualTo("U2");
        verify(userMapper).mapToEntity(eq(request), anyInt());
    }

    @Test
    void shouldThrowBusinessErrorIfEmailExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "firstName",
                "lastName",
                "1234567890",
                "test@example.com",
                "TestUser");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Business Error : Email already exists");

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnUserSuccessfully() {
        // Arrange
        String userId = "USER123";
        User user = new User();
        user.setUserId(userId);
        user.setEmail("test@example.com");
        UUID userInternalId = UUID.randomUUID();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapToResponseDto(user))
                .thenReturn(new UserResponse("USER123",
                        userInternalId,
                        "firstName",
                        "lastName",
                        "2134567890",
                        "test@example.com"));


        // Act
        UserResponse response = userService.getUser(userId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(userId);

        verify(userRepository).findByUserId(userId);
        verify(userMapper).mapToResponseDto(user);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        // Arrange
        String userId = "USER_NOT_FOUND";

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User Not found");

        verify(userRepository).findByUserId(userId);
    }

    @Test
    void shouldReturnUserSuccessfullyWhenGetUserByInternalId() {
        // Arrange
        UUID internalId = UUID.randomUUID();
        User user = new User();
        user.setId(internalId);
        user.setEmail("test@example.com");
        UUID userInternalId = UUID.randomUUID();

        when(userRepository.findById(internalId)).thenReturn(Optional.of(user));
        when(userMapper.mapToResponseDto(user))
                .thenReturn(new UserResponse("USER123",
                        userInternalId,
                        "firstName",
                        "lastName",
                        "2134567890",
                        "test@example.com"));

        // Act
        UserResponse response = userService.getUserByInternalId(internalId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("test@example.com");

        verify(userRepository).findById(internalId);
        verify(userMapper).mapToResponseDto(user);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundWhenGetUserByInternalId() {
        // Arrange
        UUID internalId = UUID.randomUUID();

        when(userRepository.findById(internalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserByInternalId(internalId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User Not found");

        verify(userRepository).findById(internalId);
    }
}

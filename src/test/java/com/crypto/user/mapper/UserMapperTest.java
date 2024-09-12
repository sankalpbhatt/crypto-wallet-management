package com.crypto.user.mapper;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.entity.User;
import com.crypto.user.utils.HashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    private User user;
    private CreateUserRequest createUserRequest;
    private int hashIterations;

    @BeforeEach
    void setUp() {
        hashIterations = 1000;
        user = new User(
                "Tom",
                "Hardy",
                "+1234567890",
                "tom.hardy@example.com",
                "hashedPassword", hashIterations);
        user.setUserId("USER12345");

        createUserRequest = new CreateUserRequest(
                "Tom",
                "Hardy",
                "+1234567890",
                "tom.hardy@example.com",
                "password123");
    }

    @Test
    void shouldMapToResponseDtoSuccessfully() {
        // Act
        UserResponse userResponse = userMapper.mapToResponseDto(user);

        // Assert
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.id()).isEqualTo(user.getUserId());
        assertThat(userResponse.internalId()).isEqualTo(user.getId());
        assertThat(userResponse.firstName()).isEqualTo(user.getFirstName());
        assertThat(userResponse.lastName()).isEqualTo(user.getLastName());
        assertThat(userResponse.phone()).isEqualTo(user.getPhone());
        assertThat(userResponse.email()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldMapToEntitySuccessfully() {
        // Act
        User mappedUser = userMapper.mapToEntity(createUserRequest, hashIterations);

        // Assert
        assertThat(mappedUser).isNotNull();
        assertThat(mappedUser.getFirstName()).isEqualTo(createUserRequest.firstName());
        assertThat(mappedUser.getLastName()).isEqualTo(createUserRequest.lastName());
        assertThat(mappedUser.getPhone()).isEqualTo(createUserRequest.phone());
        assertThat(mappedUser.getEmail()).isEqualTo(createUserRequest.email());
        assertThat(mappedUser.getPassword()).isEqualTo(HashUtils.hashPhrase(createUserRequest.password(), hashIterations));
    }
}

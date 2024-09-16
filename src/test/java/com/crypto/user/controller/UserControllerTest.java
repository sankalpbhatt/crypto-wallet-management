package com.crypto.user.controller;

import com.crypto.user.dto.response.UserResponse;
import com.crypto.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        userController = new UserController(userService);
    }

    @Test
    void shouldGetUser() {
        UserResponse userResponse = new UserResponse("U001",
                UUID.randomUUID(),
                "Tom",
                "Hardy",
                "9089786756",
                "qwe@gmail.com");
        when(userService.getUser(any())).thenReturn(userResponse);

        UserResponse actualResponse = userController.getUser("U001");

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(userResponse);
        verify(userService).getUser("U001");
    }
}
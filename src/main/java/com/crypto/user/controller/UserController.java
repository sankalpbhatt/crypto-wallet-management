package com.crypto.user.controller;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(CreateUserRequest request){
        return userService.createUser(request);
    }
}

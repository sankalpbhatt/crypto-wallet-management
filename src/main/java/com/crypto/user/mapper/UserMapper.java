package com.crypto.user.mapper;

import com.crypto.user.dto.CreateUserRequest;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.entity.User;
import com.crypto.user.utils.HashUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse mapToResponseDto(User user){
        return new UserResponse(
                user.getUserId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail());
    }

    public User mapToEntity(CreateUserRequest request, int hashIterations){
        return new User(request.firstName(),
                request.lastName(),
                request.phone(),
                request.email(),
                HashUtils.hashPhrase(request.password(), hashIterations),
                hashIterations);
    }
}

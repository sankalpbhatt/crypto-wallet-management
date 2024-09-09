package com.crypto.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record CreateUserRequest (

    @Schema(description = "First name of the user")
    String firstName,
    @Schema(description = "Last name of the user")
    String lastName,
    @Schema(description = "Phone number of the user")
    String phone,
    @Schema(description = "Email of the user")
    String email,
    @Schema(hidden = true)
    String password
){}

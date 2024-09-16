package com.crypto.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema
public record UserResponse(
        String id,
        @JsonIgnore UUID internalId,
        String firstName,
        String lastName,
        String phone,
        String email
) {
}

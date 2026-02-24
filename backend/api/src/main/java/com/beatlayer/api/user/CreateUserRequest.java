package com.beatlayer.api.user;

public record CreateUserRequest(
        String handle,
        String email
) {}

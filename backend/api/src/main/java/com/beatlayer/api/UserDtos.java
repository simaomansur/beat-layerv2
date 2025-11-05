package com.beatlayer.api;

import java.time.Instant;
import java.util.UUID;

public class UserDtos {

  public record RegisterUserRequest(
      String handle,
      String email,
      String password
  ) {}

  public record UserResponse(
      UUID id,
      String handle,
      String email,
      Instant createdAt
  ) {}

  public static UserResponse fromEntity(User u) {
    return new UserResponse(
        u.getId(),
        u.getHandle(),
        u.getEmail(),
        u.getCreatedAt()
    );
  }
}

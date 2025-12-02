package com.beatlayer.api.auth;

import java.util.UUID;
import java.time.Instant;

public class UserDtos {

  public record RegisterRequest(
      String handle,
      String email,
      String password
  ) {}

  public record LoginRequest(
      String email,
      String password
  ) {}

  public record UserResponse(
      UUID id,
      String handle,
      String email,
      Instant createdAt
  ) {}

  public record AuthResponse(
    String token,
    UserResponse user
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
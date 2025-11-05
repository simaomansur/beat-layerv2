package com.beatlayer.api;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

  private final UserRepository userRepo;

  public UserController(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  // For now: always return the dev user as "me"
  @GetMapping("/me")
  public UserDtos.UserResponse me() {
    UUID devId = UUID.fromString("00000000-0000-0000-0000-000000000001");

    User dev = userRepo.findById(devId)
        .orElseThrow(() -> new RuntimeException("Dev user not found"));

    return UserDtos.fromEntity(dev);
  }
}

package com.beatlayer.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDtos.RegisterUserRequest req) {

        // basic validation
        if (req.handle() == null || req.handle().isBlank() ||
            req.email() == null || req.email().isBlank() ||
            req.password() == null || req.password().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("handle, email, and password are required.");
        }

        if (userRepo.existsByHandle(req.handle())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Handle is already taken.");
        }

        if (userRepo.existsByEmail(req.email())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email is already registered.");
        }

        User u = new User();
        u.setHandle(req.handle().trim());
        u.setEmail(req.email().trim().toLowerCase());
        u.setPasswordHash(passwordEncoder.encode(req.password()));
        // createdAt handled by @PrePersist on User

        u = userRepo.save(u);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserDtos.fromEntity(u));
    }
}

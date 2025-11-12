package com.beatlayer.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDtos.RegisterRequest req) {

        if (req.handle() == null || req.handle().isBlank() ||
            req.email() == null || req.email().isBlank() ||
            req.password() == null || req.password().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("handle, email, and password are required.");
        }

        try {
            User newUser = userService.registerNewUser(
                req.handle().trim(),
                req.email().trim().toLowerCase(),
                req.password()
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UserDtos.fromEntity(newUser));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDtos.LoginRequest req) {
        if (req.email() == null || req.email().isBlank() ||
            req.password() == null || req.password().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("email and password are required.");
        }

        try {
            User user = userService.authenticate(req.email().trim().toLowerCase(), req.password());
            return ResponseEntity.ok(UserDtos.fromEntity(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}

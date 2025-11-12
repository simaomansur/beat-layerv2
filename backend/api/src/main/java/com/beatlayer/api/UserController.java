package com.beatlayer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDtos.UserResponse> register(
            @RequestBody UserDtos.RegisterRequest req) {
        User newUser = userService.registerNewUser(
                req.handle(), req.email(), req.password());
        return ResponseEntity.ok(UserDtos.fromEntity(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDtos.UserResponse> login(
            @RequestBody UserDtos.LoginRequest req) {
        User user = userService.authenticate(req.email(), req.password());
        return ResponseEntity.ok(UserDtos.fromEntity(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDtos.UserResponse> me(/* inject auth principal later */) {
        // TODO: get current user from security context
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

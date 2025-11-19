package com.beatlayer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDtos.UserResponse> register(
            @RequestBody UserDtos.RegisterRequest req) {
        User newUser = userService.registerNewUser(
                req.handle(), req.email(), req.password());
        return ResponseEntity.ok(UserDtos.fromEntity(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDtos.AuthResponse> login(
            @RequestBody UserDtos.LoginRequest req) {

        User user = userService.authenticate(req.email(), req.password());

        // create token
        String token = jwtUtil.createToken(user.getId(), user.getEmail());

        return ResponseEntity.ok(new UserDtos.AuthResponse(
            token,
            UserDtos.fromEntity(user)
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDtos.UserResponse> me(/* inject auth principal later */) {
        // TODO: get current user from security context
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

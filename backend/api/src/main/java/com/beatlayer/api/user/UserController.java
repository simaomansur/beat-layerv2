package com.beatlayer.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody CreateUserRequest req) {
        if (req == null || req.handle() == null || req.handle().isBlank()) {
            throw new IllegalArgumentException("handle is required");
        }

        User u = new User(req.handle().trim(), req.email() == null ? null : req.email().trim());
        return UserResponse.from(users.save(u));
    }

    @GetMapping
    public List<UserResponse> list() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }
}

package com.beatlayer.api;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User registerNewUser(String handle, String email, String rawPassword) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already in use");
    }
    if (userRepository.existsByHandle(handle)) {
      throw new IllegalArgumentException("Handle already in use");
    }

    User u = new User();
    u.setHandle(handle);
    u.setEmail(email);
    u.setPasswordHash(passwordEncoder.encode(rawPassword));
    return userRepository.save(u);
  }

  public User authenticate(String email, String rawPassword) {
    User u = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    if (!passwordEncoder.matches(rawPassword, u.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    return u;
  }
}

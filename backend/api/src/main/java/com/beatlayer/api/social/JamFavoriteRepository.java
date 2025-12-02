package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.jam.Jam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JamFavoriteRepository extends JpaRepository<JamFavorite, JamFavoriteId> {

  Optional<JamFavorite> findByJamAndUser(Jam jam, User user);

  boolean existsByJamAndUser(Jam jam, User user);

  long countByJam(Jam jam);

  void deleteByJamAndUser(Jam jam, User user);

  // If you ever want pure UUID-based methods:
  boolean existsById(JamFavoriteId id);

  default boolean existsByJamIdAndUserId(UUID jamId, UUID userId) {
    return existsById(new JamFavoriteId(jamId, userId));
  }
}

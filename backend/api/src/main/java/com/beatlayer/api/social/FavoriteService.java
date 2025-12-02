package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.auth.UserRepository;
import com.beatlayer.api.common.NotFoundException;
import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.jam.JamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FavoriteService {

  private final JamFavoriteRepository jamFavoriteRepository;
  private final JamRepository jamRepository;
  private final UserRepository userRepository;

  public FavoriteService(
      JamFavoriteRepository jamFavoriteRepository,
      JamRepository jamRepository,
      UserRepository userRepository
  ) {
    this.jamFavoriteRepository = jamFavoriteRepository;
    this.jamRepository = jamRepository;
    this.userRepository = userRepository;
  }

  private Jam getJamOrThrow(UUID jamId) {
    return jamRepository.findById(jamId)
        .orElseThrow(() -> new NotFoundException("Jam with id " + jamId + " not found"));
  }

  @Transactional
  public boolean favoriteJam(UUID jamId, User currentUser) {
    Jam jam = getJamOrThrow(jamId);

    if (jamFavoriteRepository.existsByJamAndUser(jam, currentUser)) {
      return false; // already favorited, no-op
    }

    JamFavorite favorite = new JamFavorite(jam, currentUser);
    jamFavoriteRepository.save(favorite);
    return true;
  }

  @Transactional
  public boolean unfavoriteJam(UUID jamId, User currentUser) {
    Jam jam = getJamOrThrow(jamId);

    return jamFavoriteRepository.findByJamAndUser(jam, currentUser)
        .map(fav -> {
          jamFavoriteRepository.delete(fav);
          return true;
        })
        .orElse(false);
  }

  public long countFavorites(UUID jamId) {
    Jam jam = getJamOrThrow(jamId);
    return jamFavoriteRepository.countByJam(jam);
  }

  public boolean hasUserFavorited(UUID jamId, User currentUser) {
    if (currentUser == null) {
      return false;
    }
    Jam jam = getJamOrThrow(jamId);
    return jamFavoriteRepository.existsByJamAndUser(jam, currentUser);
  }
}

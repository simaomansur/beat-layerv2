package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/jams/{jamId}/favorite")
@CrossOrigin(origins = "http://localhost:5173")
public class JamFavoriteController {

  private final FavoriteService favoriteService;

  public JamFavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  // Mark as favorite
  @PostMapping
  public ResponseEntity<?> favorite(
      @PathVariable UUID jamId,
      @AuthenticationPrincipal User currentUser
  ) {
    boolean created = favoriteService.favoriteJam(jamId, currentUser);
    return created
        ? ResponseEntity.status(201).build()
        : ResponseEntity.ok().build();
  }

  // Remove favorite
  @DeleteMapping
  public ResponseEntity<?> unfavorite(
      @PathVariable UUID jamId,
      @AuthenticationPrincipal User currentUser
  ) {
    boolean removed = favoriteService.unfavoriteJam(jamId, currentUser);
    return removed
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  // Get count + whether current user favorited
  @GetMapping
  public Map<String, Object> getStatus(
      @PathVariable UUID jamId,
      @AuthenticationPrincipal User currentUser
  ) {
    long count = favoriteService.countFavorites(jamId);
    boolean favorited = favoriteService.hasUserFavorited(jamId, currentUser);
    return Map.of(
        "count", count,
        "favorited", favorited
    );
  }
}

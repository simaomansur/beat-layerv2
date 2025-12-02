package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/layers/{layerId}/votes")
public class LayerVoteController {

  private final LikeService likeService;

  public LayerVoteController(LikeService likeService) {
    this.likeService = likeService;
  }

  // POST /layers/{layerId}/votes → add a vote (like)
  @PostMapping
  public ResponseEntity<?> voteLayer(
      @PathVariable UUID layerId,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      return ResponseEntity.status(401).body("Not authenticated");
    }

    likeService.likeLayer(layerId, currentUser);
    return ResponseEntity.ok().build();
  }

  // DELETE /layers/{layerId}/votes → remove vote (unlike)
  @DeleteMapping
  public ResponseEntity<?> unvoteLayer(
      @PathVariable UUID layerId,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      return ResponseEntity.status(401).body("Not authenticated");
    }

    likeService.unlikeLayer(layerId, currentUser);
    return ResponseEntity.noContent().build();
  }

  // GET /layers/{layerId}/votes → count + whether current user has voted
  @GetMapping
  public ResponseEntity<?> getVotes(
      @PathVariable UUID layerId,
      @AuthenticationPrincipal User currentUser
  ) {
    long count = likeService.countVotes(layerId);
    boolean voted = currentUser != null && likeService.hasUserVoted(layerId, currentUser);

    return ResponseEntity.ok(new LayerVoteResponse(count, voted));
  }

  // Simple response DTO
  public record LayerVoteResponse(long count, boolean voted) { }
}

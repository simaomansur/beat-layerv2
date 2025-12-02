package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.common.NotFoundException;
import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.jam.JamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/jams/{jamId}/likes")
@CrossOrigin(origins = "http://localhost:5173")
public class JamLikeController {

  private final JamLikeRepository jamLikeRepo;
  private final JamRepository jamRepo;

  public JamLikeController(JamLikeRepository jamLikeRepo, JamRepository jamRepo) {
    this.jamLikeRepo = jamLikeRepo;
    this.jamRepo = jamRepo;
  }

  @GetMapping("/count")
  public long getLikeCount(@PathVariable UUID jamId) {
    return jamLikeRepo.countByJam_Id(jamId);
  }

  @GetMapping("/me")
  public boolean hasCurrentUserLiked(
      @PathVariable UUID jamId,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
    }
    return jamLikeRepo.existsByJam_IdAndUser_Id(jamId, currentUser.getId());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void like(
      @PathVariable UUID jamId,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
    }

    Jam jam = jamRepo.findById(jamId)
        .orElseThrow(() -> new NotFoundException("Jam with id " + jamId + " not found"));

    boolean alreadyLiked =
        jamLikeRepo.existsByJam_IdAndUser_Id(jam.getId(), currentUser.getId());
    if (alreadyLiked) {
      return;
    }

    JamLike like = new JamLike();
    like.setJam(jam);
    like.setUser(currentUser);
    jamLikeRepo.save(like);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unlike(
      @PathVariable UUID jamId,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
    }

    jamLikeRepo.deleteByJam_IdAndUser_Id(jamId, currentUser.getId());
  }
}

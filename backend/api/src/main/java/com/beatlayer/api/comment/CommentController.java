package com.beatlayer.api.comment;

import com.beatlayer.api.auth.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  // List comments for a jam
  @GetMapping("/jams/{jamId}/comments")
  public List<CommentDtos.CommentResponse> listForJam(@PathVariable UUID jamId) {
    return commentService.listForJam(jamId);
  }

  // Create a comment on a jam
  @PostMapping("/jams/{jamId}/comments")
  public ResponseEntity<CommentDtos.CommentResponse> create(
      @PathVariable UUID jamId,
      @RequestBody CommentDtos.CreateCommentRequest req,
      @AuthenticationPrincipal User currentUser
  ) {
    CommentDtos.CommentResponse created = commentService.createComment(jamId, req, currentUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  // Update a comment
  @PutMapping("/comments/{commentId}")
  public CommentDtos.CommentResponse update(
      @PathVariable UUID commentId,
      @RequestBody CommentDtos.UpdateCommentRequest req,
      @AuthenticationPrincipal User currentUser
  ) {
    return commentService.updateComment(commentId, req, currentUser);
  }

  // Delete a comment
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID commentId,
      @AuthenticationPrincipal User currentUser
  ) {
    commentService.deleteComment(commentId, currentUser);
    return ResponseEntity.noContent().build();
  }
}

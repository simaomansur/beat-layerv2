package com.beatlayer.api.comment;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.common.NotFoundException;
import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.jam.JamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

  private final JamCommentRepository commentRepo;
  private final JamRepository jamRepo;

  public CommentService(JamCommentRepository commentRepo, JamRepository jamRepo) {
    this.commentRepo = commentRepo;
    this.jamRepo = jamRepo;
  }

  private Jam getJamOrThrow(UUID jamId) {
    return jamRepo.findById(jamId)
        .orElseThrow(() -> new NotFoundException("Jam with id " + jamId + " not found"));
  }

  private JamComment getCommentOrThrow(UUID id) {
    return commentRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));
  }

  @Transactional
  public CommentDtos.CommentResponse createComment(
      UUID jamId,
      CommentDtos.CreateCommentRequest req,
      User currentUser
  ) {
    Jam jam = getJamOrThrow(jamId);

    JamComment comment = new JamComment();
    comment.setJam(jam);
    comment.setUser(currentUser);
    comment.setBody(req.body());

    if (req.parentCommentId() != null) {
      JamComment parent = getCommentOrThrow(req.parentCommentId());
      comment.setParentComment(parent);
    }

    JamComment saved = commentRepo.save(comment);
    return CommentDtos.fromEntity(saved);
  }

  @Transactional(readOnly = true)
  public List<CommentDtos.CommentResponse> listForJam(UUID jamId) {
    Jam jam = getJamOrThrow(jamId);
    return commentRepo.findByJamOrderByCreatedAtAsc(jam)
        .stream()
        .map(CommentDtos::fromEntity)
        .toList();
  }

  @Transactional
  public CommentDtos.CommentResponse updateComment(
      UUID commentId,
      CommentDtos.UpdateCommentRequest req,
      User currentUser
  ) {
    JamComment comment = getCommentOrThrow(commentId);

    if (!comment.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("You can only edit your own comments");
      // you can switch to a custom ForbiddenException if you have one
    }

    if (req.body() != null && !req.body().isBlank()) {
      comment.setBody(req.body());
    }

    JamComment saved = commentRepo.save(comment);
    return CommentDtos.fromEntity(saved);
  }

  @Transactional
  public void deleteComment(UUID commentId, User currentUser) {
    JamComment comment = getCommentOrThrow(commentId);

    if (!comment.getUser().getId().equals(currentUser.getId())) {
      throw new RuntimeException("You can only delete your own comments");
    }

    commentRepo.delete(comment);
  }
}

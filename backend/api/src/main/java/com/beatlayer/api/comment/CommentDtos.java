package com.beatlayer.api.comment;

import java.time.Instant;
import java.util.UUID;

public class CommentDtos {

  public record CreateCommentRequest(
      String body,
      UUID parentCommentId
  ) {}

  public record UpdateCommentRequest(
      String body
  ) {}

  public record CommentResponse(
      UUID id,
      UUID jamId,
      UUID userId,
      String username,
      String body,
      UUID parentCommentId,
      Instant createdAt,
      Instant updatedAt
  ) {}

  public static CommentResponse fromEntity(JamComment c) {
    return new CommentResponse(
        c.getId(),
        c.getJam().getId(),
        c.getUser().getId(),
        c.getUser().getHandle(),
        c.getBody(),
        c.getParentComment() != null ? c.getParentComment().getId() : null,
        c.getCreatedAt(),
        c.getUpdatedAt()
    );
  }
}

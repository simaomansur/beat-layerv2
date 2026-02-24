package com.beatlayer.api.thread;

import java.util.UUID;

public record CreateCommentReplyRequest(
        UUID createdByUserId,
        String body
) {}

package com.beatlayer.api.thread;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> findByUserIdAndTargetIdAndReactionType(UUID userId, UUID targetId, String reactionType);

    long countByTargetIdAndReactionType(UUID targetId, String reactionType);
}

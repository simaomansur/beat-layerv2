package com.beatlayer.api.social;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LayerVoteRepository extends JpaRepository<LayerVote, LayerVoteId> {

  long countByLayerId(UUID layerId);

  boolean existsByLayerIdAndUserId(UUID layerId, UUID userId);

  void deleteByLayerIdAndUserId(UUID layerId, UUID userId);
}

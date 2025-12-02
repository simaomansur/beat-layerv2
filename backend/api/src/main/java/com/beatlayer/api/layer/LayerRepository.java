package com.beatlayer.api.layer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LayerRepository extends JpaRepository<Layer, UUID> {

  List<Layer> findByJamIdOrderByCreatedAtAsc(UUID jamId);

  List<Layer> findByJamIdAndParentLayerIsNullOrderByCreatedAtAsc(UUID jamId);
}

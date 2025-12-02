package com.beatlayer.api.social;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JamLikeRepository extends JpaRepository<JamLike, JamLikeId> {

  long countByJam_Id(UUID jamId);

  boolean existsByJam_IdAndUser_Id(UUID jamId, UUID userId);

  void deleteByJam_IdAndUser_Id(UUID jamId, UUID userId);
}

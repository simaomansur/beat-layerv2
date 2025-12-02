package com.beatlayer.api.jam;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JamTagRepository extends JpaRepository<JamTag, JamTagId> {

  // All tags for a jam
  List<JamTag> findByJamId(UUID jamId);

  // All jam-tag rows for a given tag
  List<JamTag> findByTagId(UUID tagId);

  // Quick existence check (useful to prevent duplicates)
  boolean existsByJamIdAndTagId(UUID jamId, UUID tagId);

  // Remove a single tag from a jam
  void deleteByJamIdAndTagId(UUID jamId, UUID tagId);

  // Remove all tags from a jam (e.g. when resetting tags)
  void deleteByJamId(UUID jamId);
}

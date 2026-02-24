package com.beatlayer.api.thread;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ThreadItemRepository extends JpaRepository<ThreadItem, UUID> {
    List<ThreadItem> findByJamIdOrderByCreatedAtAsc(UUID jamId);
}

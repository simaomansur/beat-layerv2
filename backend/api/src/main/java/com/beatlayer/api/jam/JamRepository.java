package com.beatlayer.api.jam;

import com.beatlayer.api.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JamRepository extends JpaRepository<Jam, UUID>, JpaSpecificationExecutor<Jam> {

  // Handy for "my jams" pages later
  Page<Jam> findByCreatedBy(User user, Pageable pageable);
}

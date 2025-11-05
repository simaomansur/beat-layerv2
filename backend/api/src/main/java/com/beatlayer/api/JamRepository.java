package com.beatlayer.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JamRepository extends JpaRepository<Jam, UUID>, JpaSpecificationExecutor<Jam> {
}

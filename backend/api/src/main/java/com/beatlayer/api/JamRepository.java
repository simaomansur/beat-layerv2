package com.beatlayer.api;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

interface JamRepository extends JpaRepository<Jam, UUID> {}

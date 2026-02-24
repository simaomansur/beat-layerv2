package com.beatlayer.api.audio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AudioItemDetailsRepository extends JpaRepository<AudioItemDetails, UUID> {
}

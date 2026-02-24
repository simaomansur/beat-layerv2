package com.beatlayer.api.audio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AudioAssetRepository extends JpaRepository<AudioAsset, UUID> {
}

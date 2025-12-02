package com.beatlayer.api.layer;

import java.time.Instant;
import java.util.UUID;
import java.math.BigDecimal;

public class LayerDtos {

  public record LayerResponse(
      UUID id,
      UUID jamId,
      UUID parentLayerId,
      UUID createdById,
      String s3KeyOriginal,
      String s3KeyNormalized,
      Integer durationMs,
      Integer bars,
      Integer startOffsetMs,
      BigDecimal gainDb,
      BigDecimal pan,
      String keyOverride,
      Integer bpmOverride,
      String instrument,
      String notes,
      Instant createdAt
  ) {}

  public record CreateLayerRequest(
      UUID parentLayerId,
      String s3KeyOriginal,
      String s3KeyNormalized,
      Integer durationMs,
      Integer bars,
      Integer startOffsetMs,
      BigDecimal gainDb,
      BigDecimal pan,
      String keyOverride,
      Integer bpmOverride,
      String instrument,
      String notes
  ) {}

  public static LayerResponse fromEntity(Layer layer) {
    return new LayerResponse(
        layer.getId(),
        layer.getJam() != null ? layer.getJam().getId() : null,
        layer.getParentLayer() != null ? layer.getParentLayer().getId() : null,
        layer.getCreatedBy() != null ? layer.getCreatedBy().getId() : null,
        layer.getS3KeyOriginal(),
        layer.getS3KeyNormalized(),
        layer.getDurationMs(),
        layer.getBars(),
        layer.getStartOffsetMs(),
        layer.getGainDb(),
        layer.getPan(),
        layer.getKeyOverride(),
        layer.getBpmOverride(),
        layer.getInstrument(),
        layer.getNotes(),
        layer.getCreatedAt()
    );
  }
}

package com.beatlayer.api.layer;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.common.NotFoundException;
import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.jam.JamRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jams/{jamId}/layers")
@CrossOrigin(origins = "http://localhost:5173")
public class LayerController {

  private final LayerRepository layerRepo;
  private final JamRepository jamRepo;

  public LayerController(LayerRepository layerRepo, JamRepository jamRepo) {
    this.layerRepo = layerRepo;
    this.jamRepo = jamRepo;
  }

  // List all layers for a jam
  @GetMapping
  public List<LayerDtos.LayerResponse> listForJam(@PathVariable UUID jamId) {
    List<Layer> layers = layerRepo.findByJamIdOrderByCreatedAtAsc(jamId);
    return layers.stream()
        .map(LayerDtos::fromEntity)
        .toList();
  }

  // Get one layer by id for a jam
  @GetMapping("/{layerId}")
  public LayerDtos.LayerResponse getOne(
      @PathVariable UUID jamId,
      @PathVariable UUID layerId
  ) {
    Layer layer = layerRepo.findById(layerId)
        .orElseThrow(() -> new NotFoundException("Layer with id " + layerId + " not found"));

    if (layer.getJam() == null || !layer.getJam().getId().equals(jamId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Layer not found for this jam");
    }

    return LayerDtos.fromEntity(layer);
  }

  // Create a new layer on a jam
  @PostMapping
  public LayerDtos.LayerResponse create(
      @PathVariable UUID jamId,
      @Valid @RequestBody LayerDtos.CreateLayerRequest req,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
    }

    Jam jam = jamRepo.findById(jamId)
        .orElseThrow(() -> new NotFoundException("Jam with id " + jamId + " not found"));

    if (req.s3KeyOriginal() == null || req.s3KeyOriginal().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "s3KeyOriginal is required");
    }
    if (req.durationMs() == null || req.durationMs() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "durationMs must be > 0");
    }
    if (req.bars() == null || req.bars() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bars must be > 0");
    }

    Layer parentLayer = null;
    if (req.parentLayerId() != null) {
      parentLayer = layerRepo.findById(req.parentLayerId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "parentLayerId not found"));

      // Make sure parent layer belongs to the same jam
      if (parentLayer.getJam() == null || !parentLayer.getJam().getId().equals(jamId)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent layer must belong to the same jam");
      }

      // Enforce max depth of 4 (parent chain length + 1 <= 4)
      int depth = computeDepth(parentLayer) + 1;
      if (depth > 4) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum layer depth is 4");
      }
    }

    Layer layer = new Layer();
    layer.setJam(jam);
    layer.setParentLayer(parentLayer);
    layer.setCreatedBy(currentUser);

    layer.setS3KeyOriginal(req.s3KeyOriginal());
    layer.setS3KeyNormalized(req.s3KeyNormalized());
    layer.setDurationMs(req.durationMs());
    layer.setBars(req.bars());

    if (req.startOffsetMs() != null) {
      layer.setStartOffsetMs(req.startOffsetMs());
    }

    if (req.gainDb() != null) {
      layer.setGainDb(req.gainDb());
    }

    if (req.pan() != null) {
      layer.setPan(req.pan());
    }

    layer.setKeyOverride(req.keyOverride());
    layer.setBpmOverride(req.bpmOverride());
    layer.setInstrument(req.instrument());
    layer.setNotes(req.notes());

    layer = layerRepo.save(layer);
    return LayerDtos.fromEntity(layer);
  }

  // Delete a layer (owner only, for now)
  @DeleteMapping("/{layerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable UUID jamId,
      @PathVariable UUID layerId,
      @AuthenticationPrincipal User currentUser
  ) {
    if (currentUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
    }

    Layer layer = layerRepo.findById(layerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Layer not found"));

    if (layer.getJam() == null || !layer.getJam().getId().equals(jamId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Layer not found for this jam");
    }

    if (layer.getCreatedBy() == null ||
        !layer.getCreatedBy().getId().equals(currentUser.getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your layer");
    }

    layerRepo.delete(layer);
  }

  private int computeDepth(Layer layer) {
    int depth = 0;
    Layer current = layer;
    while (current != null && depth < 16) {
      depth++;
      current = current.getParentLayer();
    }
    return depth;
  }
}

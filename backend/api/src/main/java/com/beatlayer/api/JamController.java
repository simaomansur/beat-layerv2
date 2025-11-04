package com.beatlayer.api;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jams")
@CrossOrigin(origins = "http://localhost:5173")
public class JamController {

  private final JamRepository repo;

  public JamController(JamRepository repo) {
    this.repo = repo;
  }

  // Create
  @PostMapping
  public JamDtos.JamResponse create(@Valid @RequestBody JamDtos.CreateJamRequest req) {
    Jam j = new Jam();
    j.setTitle(req.title());
    j.setKey(req.key());
    j.setBpm(req.bpm());
    j.setGenre(req.genre());
    j.setInstrumentHint(req.instrumentHint());

    // TODO: replace this with real authenticated user later
    j.setCreatedBy(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    j = repo.save(j);
    return JamDtos.fromEntity(j);
  }

  // List all
  @GetMapping
  public List<JamDtos.JamResponse> list() {
    return repo.findAll()
               .stream()
               .map(JamDtos::fromEntity)
               .toList();
  }

  // Get one by id
  @GetMapping("/{id}")
  public JamDtos.JamResponse getOne(@PathVariable UUID id) {
    Jam j = repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Jam with id " + id + " not found"));
    return JamDtos.fromEntity(j);
  }

  // Update (partial)
  @PutMapping("/{id}")
  public JamDtos.JamResponse update(
      @PathVariable UUID id,
      @Valid @RequestBody JamDtos.UpdateJamRequest req
  ) {
    Jam j = repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Jam with id " + id + " not found"));

    if (req.title() != null && !req.title().isBlank()) {
      j.setTitle(req.title());
    }
    if (req.key() != null) {
      j.setKey(req.key());
    }
    if (req.bpm() != null) {
      j.setBpm(req.bpm());
    }
    if (req.genre() != null) {
      j.setGenre(req.genre());
    }
    if (req.instrumentHint() != null) {
      j.setInstrumentHint(req.instrumentHint());
    }

    j = repo.save(j);
    return JamDtos.fromEntity(j);
  }

  // Delete
  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    if (!repo.existsById(id)) {
      throw new NotFoundException("Jam with id " + id + " not found");
    }
    repo.deleteById(id);
  }
}

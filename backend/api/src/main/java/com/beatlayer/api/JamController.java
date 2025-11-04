package com.beatlayer.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/jams")
@CrossOrigin(origins = "http://localhost:5173")
public class JamController {

  private final JamRepository repo;

  public JamController(JamRepository repo) {
    this.repo = repo;
  }

  @PostMapping
  public JamDtos.JamResponse create(@Valid @RequestBody JamDtos.CreateJamRequest req) {
    Jam j = new Jam();
    j.setTitle(req.title());
    j.setKey(req.key());
    j.setBpm(req.bpm());
    j.setGenre(req.genre());
    j.setInstrumentHint(req.instrumentHint());
    j.setCreatedBy(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    j = repo.save(j);
    return JamDtos.fromEntity(j);
  }

  // ✅ New: list all jams
  @GetMapping
  public List<JamDtos.JamResponse> list() {
    return repo.findAll().stream().map(JamDtos::fromEntity).toList();
  }

  // ✅ New: get a specific jam by id
  @GetMapping("/{id}")
  public JamDtos.JamResponse get(@PathVariable UUID id) {
    var jam = repo.findById(id).orElseThrow(() ->
        new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.NOT_FOUND, "Jam not found"));
    return JamDtos.fromEntity(jam);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
      repo.deleteById(id);
      return ResponseEntity.noContent().build();
  }
}

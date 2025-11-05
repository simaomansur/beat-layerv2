package com.beatlayer.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

  // List with pagination, sorting, filters
  @GetMapping
  public PageResponse<JamDtos.JamResponse> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt,desc") String sort,
      @RequestParam(required = false) String genre,
      @RequestParam(required = false) Integer minBpm,
      @RequestParam(required = false) Integer maxBpm,
      @RequestParam(required = false, name = "q") String query
  ) {
    // --- build Sort ---
    // sort looks like: "field,dir" e.g. "bpm,asc"
    String[] sortParts = sort.split(",");
    String sortField = sortParts[0];
    String sortDir = (sortParts.length > 1) ? sortParts[1].toLowerCase() : "desc";

    Sort.Direction direction = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    // --- build Specification ---
    Specification<Jam> spec = Specification.where(null);

    if (genre != null && !genre.isBlank()) {
      String g = genre.toLowerCase();
      spec = spec.and((root, query1, cb) ->
          cb.equal(cb.lower(root.get("genre")), g)
      );
    }

    if (minBpm != null) {
      spec = spec.and((root, query1, cb) ->
          cb.ge(root.get("bpm"), minBpm)
      );
    }

    if (maxBpm != null) {
      spec = spec.and((root, query1, cb) ->
          cb.le(root.get("bpm"), maxBpm)
      );
    }

    if (query != null && !query.isBlank()) {
      String pattern = "%" + query.toLowerCase() + "%";
      spec = spec.and((root, query1, cb) ->
          cb.or(
              cb.like(cb.lower(root.get("title")), pattern),
              cb.like(cb.lower(root.get("genre")), pattern),
              cb.like(cb.lower(root.get("instrumentHint")), pattern)
          )
      );
    }

    Page<Jam> pageResult = repo.findAll(spec, pageable);

    List<JamDtos.JamResponse> content = pageResult
        .getContent()
        .stream()
        .map(JamDtos::fromEntity)
        .toList();

    return new PageResponse<>(
        content,
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.getTotalPages(),
        pageResult.isLast()
    );
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

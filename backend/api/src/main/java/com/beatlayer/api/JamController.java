package com.beatlayer.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jams")
@CrossOrigin(origins = "http://localhost:5173")
public class JamController {

  private final JamRepository repo;
  private final UserRepository userRepo;
  private final AudioStorageService audioStorageService;

  public JamController(JamRepository repo, UserRepository userRepo, AudioStorageService audioStorageService) {
    this.repo = repo;
    this.userRepo = userRepo;
    this.audioStorageService = audioStorageService;
  }

  // Create a jam
  @PostMapping
  public JamDtos.JamResponse create(@Valid @RequestBody JamDtos.CreateJamRequest req) {

  User devUser = userRepo.findByHandle("dev")
      .orElseThrow(() -> new RuntimeException("Dev user not found"));

    Jam j = new Jam();
    j.setTitle(req.title());
    j.setKey(req.key());
    j.setBpm(req.bpm());
    j.setGenre(req.genre());
    j.setInstrumentHint(req.instrumentHint());
    j.setCreatedBy(devUser);

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
    // sort format: "field,dir" e.g. "bpm,asc"
    String[] sortParts = sort.split(",");
    String sortField = sortParts[0];
    String sortDir = (sortParts.length > 1) ? sortParts[1].toLowerCase() : "desc";

    Sort.Direction direction = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    Specification<Jam> spec = Specification.where(null);

    if (genre != null && !genre.isBlank()) {
      String g = genre.toLowerCase();
      spec = spec.and((root, q1, cb) ->
          cb.equal(cb.lower(root.get("genre")), g)
      );
    }

    if (minBpm != null) {
      spec = spec.and((root, q1, cb) ->
          cb.ge(root.get("bpm"), minBpm)
      );
    }

    if (maxBpm != null) {
      spec = spec.and((root, q1, cb) ->
          cb.le(root.get("bpm"), maxBpm)
      );
    }

    if (query != null && !query.isBlank()) {
      String pattern = "%" + query.toLowerCase() + "%";
      spec = spec.and((root, q1, cb) ->
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

  // Get one jam
  @GetMapping("/{id}")
  public JamDtos.JamResponse getOne(@PathVariable UUID id) {
    Jam j = repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Jam with id " + id + " not found"));
    return JamDtos.fromEntity(j);
  }

  // Update jam (partial)
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
    if (req.key() != null && !req.key().isBlank()) {
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

  // Delete jam
  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    if (!repo.existsById(id)) {
      throw new NotFoundException("Jam with id " + id + " not found");
    }
    repo.deleteById(id);
  }

  @PostMapping("/{jamId}/layers/base")
  public ResponseEntity<?> uploadBaseLayer(
      @PathVariable UUID jamId,
      @RequestParam("audio") MultipartFile audioFile,
      @RequestParam(name = "loopBars", required = false) Integer loopBars
  ) throws Exception {
    Jam jam = repo.findById(jamId)
        .orElseThrow(() -> new NotFoundException("Jam with id " + jamId + " not found"));

    if (audioFile.isEmpty()) {
      return ResponseEntity.badRequest().body("Audio file is required");
    }

    // 1) Store file on disk (using LocalAudioStorageService) and get its URL
    String audioUrl = audioStorageService.storeBaseLayerAudio(
        audioFile,
        jam.getId().toString()
    );

    // 2) Save URL on the jam
    jam.setBaseAudioUrl(audioUrl);
    repo.save(jam);

    System.out.println(
        "Stored base layer for jam " + jam.getId() +
        " at " + audioUrl +
        " loopBars=" + loopBars
    );

    // 3) We don't need to return anything special yet
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}

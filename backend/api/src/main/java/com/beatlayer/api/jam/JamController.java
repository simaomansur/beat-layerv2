package com.beatlayer.api.jam;

import com.beatlayer.api.user.User;
import com.beatlayer.api.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jams")
public class JamController {

    private final JamRepository jams;
    private final UserRepository users;

    public JamController(JamRepository jams, UserRepository users) {
        this.jams = jams;
        this.users = users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JamResponse create(@RequestBody CreateJamRequest req) {
        if (req == null) throw new IllegalArgumentException("body is required");
        if (req.createdByUserId() == null) throw new IllegalArgumentException("createdByUserId is required");
        if (req.title() == null || req.title().isBlank()) throw new IllegalArgumentException("title is required");
        if (req.loopLengthMs() == null || req.loopLengthMs() <= 0) throw new IllegalArgumentException("loopLengthMs must be > 0");

        User creator = users.findById(req.createdByUserId())
                .orElseThrow(() -> new IllegalArgumentException("createdByUserId not found: " + req.createdByUserId()));

        Jam jam = new Jam(creator, req.title().trim(), req.loopLengthMs());
        jam.setDescription(req.description());
        jam.setBpm(req.bpm());
        jam.setMusicalKey(req.musicalKey());
        jam.setGenre(req.genre());
        jam.setInstrumentHint(req.instrumentHint());

        if (req.visibility() != null && !req.visibility().isBlank()) {
            jam.setVisibility(req.visibility().trim());
        }

        return JamResponse.from(jams.save(jam));
    }

    @GetMapping
    public List<JamResponse> list() {
        return jams.findAll().stream().map(JamResponse::from).toList();
    }

    @GetMapping("/{id}")
    public JamResponse get(@PathVariable UUID id) {
        Jam jam = jams.findById(id).orElseThrow(() -> new IllegalArgumentException("Jam not found: " + id));
        return JamResponse.from(jam);
    }
}

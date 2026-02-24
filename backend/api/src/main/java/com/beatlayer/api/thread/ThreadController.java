package com.beatlayer.api.thread;

import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.jam.JamRepository;
import com.beatlayer.api.user.User;
import com.beatlayer.api.user.UserRepository;
import com.beatlayer.api.audio.AudioItemDetails;
import com.beatlayer.api.audio.AudioItemDetailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/thread")
public class ThreadController {

    private final ThreadItemRepository threadItems;
    private final UserRepository users;
    private final JamRepository jams;
    private final AudioItemDetailsRepository audioDetails;

    public ThreadController(
            ThreadItemRepository threadItems,
            UserRepository users,
            JamRepository jams,
            AudioItemDetailsRepository audioDetails
    ) {
        this.threadItems = threadItems;
        this.users = users;
        this.jams = jams;
        this.audioDetails = audioDetails;
    }

    @PostMapping("/{parentId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ThreadItemResponse replyWithComment(@PathVariable UUID parentId, @RequestBody CreateCommentReplyRequest req) {
        if (req == null) throw new IllegalArgumentException("body is required");
        if (req.createdByUserId() == null) throw new IllegalArgumentException("createdByUserId is required");
        if (req.body() == null || req.body().isBlank()) throw new IllegalArgumentException("body is required");

        ThreadItem parent = threadItems.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent thread item not found: " + parentId));

        User creator = users.findById(req.createdByUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.createdByUserId()));

        ThreadItem comment = ThreadItem.newComment(parent.getJam(), parent, creator, req.body().trim());
        comment = threadItems.save(comment);

        return ThreadItemResponse.from(comment);
    }

    @GetMapping("/jams/{jamId}")
    public List<ThreadItemResponse> getJamThread(@PathVariable UUID jamId) {
        // Optional sanity check so you get a clean "jam not found" message
        Jam jam = jams.findById(jamId)
                .orElseThrow(() -> new IllegalArgumentException("Jam not found: " + jamId));

        return threadItems.findByJamIdOrderByCreatedAtAsc(jam.getId())
                .stream()
                .map(ThreadItemResponse::from)
                .toList();
    }

    @GetMapping("/{threadItemId}/lineage")
    @Transactional
    public java.util.List<LineageAudioLayerResponse> getAudioLineage(@PathVariable UUID threadItemId) {

        ThreadItem current = threadItems.findById(threadItemId)
                .orElseThrow(() -> new IllegalArgumentException("Thread item not found: " + threadItemId));

        if (!"AUDIO".equals(current.getItemType())) {
            throw new IllegalArgumentException("Lineage is for AUDIO items only. Item is: " + current.getItemType());
        }

        java.util.ArrayList<LineageAudioLayerResponse> layers = new java.util.ArrayList<>();

        // Walk up parent chain: selected -> ... -> root
        ThreadItem cursor = current;
        while (cursor != null) {
            if ("AUDIO".equals(cursor.getItemType())) {
                UUID tid = cursor.getId();

                AudioItemDetails details = audioDetails.findById(tid)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Missing audio_item_details for AUDIO thread item: " + tid
                        ));

                    var asset = details.getAudioAsset();

                layers.add(new LineageAudioLayerResponse(
                        cursor.getId(),
                        cursor.getJam().getId(),
                        cursor.getParent() == null ? null : cursor.getParent().getId(),
                        cursor.getCreatedBy().getId(),
                        cursor.getCreatedAt(),

                        asset.getId(),
                        asset.getStorageLocator(),
                        asset.getMimeType(),
                        asset.getDurationMs(),

                        details.getStartOffsetMs(),
                        details.getTrimStartMs(),
                        details.getTrimEndMs(),
                        details.getGainDb(),
                        details.getPan(),
                        details.getMuted(),

                        details.getInstrument(),
                        details.getNotes()
                ));
            }

            cursor = cursor.getParent();
        }

        // Currently layers = [selected, parent, ..., root] so reverse
        java.util.Collections.reverse(layers);
        return layers;
    }
}

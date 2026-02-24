package com.beatlayer.api.audio;

import com.beatlayer.api.jam.Jam;
import com.beatlayer.api.jam.JamRepository;
import com.beatlayer.api.thread.ThreadItem;
import com.beatlayer.api.thread.ThreadItemRepository;
import com.beatlayer.api.user.User;
import com.beatlayer.api.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private final UserRepository users;
    private final JamRepository jams;
    private final ThreadItemRepository threadItems;
    private final AudioAssetRepository audioAssets;
    private final AudioItemDetailsRepository audioDetails;

    public AudioController(
            UserRepository users,
            JamRepository jams,
            ThreadItemRepository threadItems,
            AudioAssetRepository audioAssets,
            AudioItemDetailsRepository audioDetails
    ) {
        this.users = users;
        this.jams = jams;
        this.threadItems = threadItems;
        this.audioAssets = audioAssets;
        this.audioDetails = audioDetails;
    }

    @PostMapping("/jams/{jamId}/root")
    @ResponseStatus(HttpStatus.CREATED)
    public AudioThreadItemResponse createRootAudio(@PathVariable UUID jamId, @RequestBody CreateRootAudioRequest req) {

        if (req == null) throw new IllegalArgumentException("body is required");
        if (req.createdByUserId() == null) throw new IllegalArgumentException("createdByUserId is required");
        if (req.storageLocator() == null || req.storageLocator().isBlank()) throw new IllegalArgumentException("storageLocator is required");
        if (req.mimeType() == null || req.mimeType().isBlank()) throw new IllegalArgumentException("mimeType is required");
        if (req.durationMs() == null || req.durationMs() <= 0) throw new IllegalArgumentException("durationMs must be > 0");

        User creator = users.findById(req.createdByUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.createdByUserId()));

        Jam jam = jams.findById(jamId)
                .orElseThrow(() -> new IllegalArgumentException("Jam not found: " + jamId));

        if (jam.getRootItem() != null) {
            throw new IllegalArgumentException("Jam already has a root item: " + jam.getRootItem().getId());
        }

        // 1) create root AUDIO thread item
        ThreadItem root = ThreadItem.newAudio(jam, null, creator);
        root = threadItems.save(root);

        // 2) create audio asset
        AudioAsset asset = new AudioAsset(creator, req.storageLocator().trim(), req.mimeType().trim(), req.durationMs());
        asset.setSampleRate(req.sampleRate());
        asset.setChannels(req.channels());
        asset.setBytes(req.bytes());
        asset = audioAssets.save(asset);

        // 3) create audio item details (defaults already set)
        AudioItemDetails details = new AudioItemDetails(root, asset);
        details.setInstrument(req.instrument());
        details.setNotes(req.notes());
        audioDetails.save(details);

        // 4) attach to jam
        jam.setRootItem(root);
        jams.save(jam);

        return new AudioThreadItemResponse(
                jam.getId(),
                root.getId(),
                asset.getId(),
                creator.getId(),
                root.getItemType(),
                null,
                asset.getStorageLocator(),
                asset.getMimeType(),
                asset.getDurationMs(),
                root.getCreatedAt()
        );
    }

    @PostMapping("/thread/{parentId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public AudioThreadItemResponse replyWithAudio(@PathVariable UUID parentId, @RequestBody CreateAudioReplyRequest req) {

        if (req == null) throw new IllegalArgumentException("body is required");
        if (req.createdByUserId() == null) throw new IllegalArgumentException("createdByUserId is required");
        if (req.storageLocator() == null || req.storageLocator().isBlank()) throw new IllegalArgumentException("storageLocator is required");
        if (req.mimeType() == null || req.mimeType().isBlank()) throw new IllegalArgumentException("mimeType is required");
        if (req.durationMs() == null || req.durationMs() <= 0) throw new IllegalArgumentException("durationMs must be > 0");

        ThreadItem parent = threadItems.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent thread item not found: " + parentId));

        User creator = users.findById(req.createdByUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.createdByUserId()));

        // 1) Create AUDIO thread item as child of parent (branching!)
        ThreadItem audioItem = ThreadItem.newAudio(parent.getJam(), parent, creator);
        audioItem = threadItems.save(audioItem);

        // 2) Create audio asset
        AudioAsset asset = new AudioAsset(creator, req.storageLocator().trim(), req.mimeType().trim(), req.durationMs());
        asset.setSampleRate(req.sampleRate());
        asset.setChannels(req.channels());
        asset.setBytes(req.bytes());
        asset = audioAssets.save(asset);

        // 3) Create audio details and apply optional edits
        AudioItemDetails details = new AudioItemDetails(audioItem, asset);

        if (req.startOffsetMs() != null) details.setStartOffsetMs(req.startOffsetMs());
        if (req.trimStartMs() != null) details.setTrimStartMs(req.trimStartMs());
        if (req.trimEndMs() != null) details.setTrimEndMs(req.trimEndMs());

        details.setInstrument(req.instrument());
        details.setNotes(req.notes());

        audioDetails.save(details);

        return new AudioThreadItemResponse(
                parent.getJam().getId(),
                audioItem.getId(),
                asset.getId(),
                creator.getId(),
                audioItem.getItemType(),
                parent.getId(),
                asset.getStorageLocator(),
                asset.getMimeType(),
                asset.getDurationMs(),
                audioItem.getCreatedAt()
        );
    }
}
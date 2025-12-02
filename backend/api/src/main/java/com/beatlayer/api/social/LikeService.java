package com.beatlayer.api.social;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.layer.Layer;
import com.beatlayer.api.layer.LayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LikeService {

  private final LayerRepository layerRepository;
  private final LayerVoteRepository layerVoteRepository;

  public LikeService(LayerRepository layerRepository,
                     LayerVoteRepository layerVoteRepository) {
    this.layerRepository = layerRepository;
    this.layerVoteRepository = layerVoteRepository;
  }

  @Transactional
  public void likeLayer(UUID layerId, User user) {
    if (user == null) {
      throw new IllegalArgumentException("User must be logged in to like a layer");
    }

    if (layerVoteRepository.existsByLayerIdAndUserId(layerId, user.getId())) {
      // already liked, do nothing
      return;
    }

    Layer layer = layerRepository.findById(layerId)
        .orElseThrow(() -> new RuntimeException("Layer not found"));

    LayerVote vote = new LayerVote();
    vote.setLayerId(layer.getId());
    vote.setUserId(user.getId());
    vote.setLayer(layer);
    vote.setUser(user);

    layerVoteRepository.save(vote);
  }

  @Transactional
  public void unlikeLayer(UUID layerId, User user) {
    if (user == null) {
      throw new IllegalArgumentException("User must be logged in to unlike a layer");
    }
    layerVoteRepository.deleteByLayerIdAndUserId(layerId, user.getId());
  }

  public long countVotes(UUID layerId) {
    return layerVoteRepository.countByLayerId(layerId);
  }

  public boolean hasUserVoted(UUID layerId, User user) {
    if (user == null) {
      return false;
    }
    return layerVoteRepository.existsByLayerIdAndUserId(layerId, user.getId());
  }
}

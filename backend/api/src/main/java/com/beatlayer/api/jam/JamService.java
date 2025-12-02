package com.beatlayer.api.jam;

import com.beatlayer.api.auth.User;
import com.beatlayer.api.common.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JamService {

  private final JamRepository jamRepository;

  public JamService(JamRepository jamRepository) {
    this.jamRepository = jamRepository;
  }

  public Jam getByIdOrThrow(UUID id) {
    return jamRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Jam with id " + id + " not found"));
  }

  public Jam save(Jam jam) {
    return jamRepository.save(jam);
  }

  public Page<Jam> findAll(Specification<Jam> spec, Pageable pageable) {
    if (spec == null) {
      return jamRepository.findAll(pageable);
    }
    return jamRepository.findAll(spec, pageable);
  }

  public Page<Jam> findByCreator(User user, Pageable pageable) {
    return jamRepository.findByCreatedBy(user, pageable);
  }
}

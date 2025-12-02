package com.beatlayer.api.comment;

import com.beatlayer.api.jam.Jam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JamCommentRepository extends JpaRepository<JamComment, UUID> {

  List<JamComment> findByJamOrderByCreatedAtAsc(Jam jam);

  List<JamComment> findByJamAndParentCommentIsNullOrderByCreatedAtAsc(Jam jam);

  List<JamComment> findByParentCommentOrderByCreatedAtAsc(JamComment parent);
}

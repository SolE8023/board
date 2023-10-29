package com.hansol.board.comment.repository;

import com.hansol.board.comment.domain.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepository {
    CommentEntity save(CommentEntity comment);
    CommentEntity update(CommentEntity comment);
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
    Optional<CommentEntity> findByIdAndPassword(Long id, String password);

    Optional<CommentEntity> findById(Long id);
    Boolean passwordCheck(Long id, String password);
    void delete(Long id);
}

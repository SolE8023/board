package com.hansol.board.comment.repository;

import com.hansol.board.comment.domain.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);

    Optional<CommentEntity> findByIdAndPassword(Long id, String password);
}

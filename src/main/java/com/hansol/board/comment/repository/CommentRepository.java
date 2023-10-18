package com.hansol.board.comment.repository;

import com.hansol.board.comment.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findByPostId(Long id);
    Optional<Comment> findByIdAndPassword(Long id, String password);
    Boolean passwordCheck(Long id, String password);
    void remove(Long id, String password);
}

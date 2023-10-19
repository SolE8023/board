package com.hansol.board.post.repository;

import com.hansol.board.post.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long postId);

    Post save(Post post);

    Post update(Post post, String password);

    Optional<Post> findByIdAndAndPassword(Long id, String password);

    List<Post> findOrderByNotice();

    void remove(Long id, String password);

    List<Post> findByConditions(String condition, String search);

    Boolean passwordCheck(Long id, String password);

    List<Post> findOrderByCreatedAt();

    Boolean isSecretPost(Long id);

    List<Post> findAll();
}

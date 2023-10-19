package com.hansol.board.post.repository;

import com.hansol.board.post.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long postId);

    Post save(Post post);

    Post update(Post post, String password);

    Optional<Post> findByIdAndAndPassword(Long id, String password);

    Page<Post> findListOrderby(int page, String code);

    void remove(Long id, String password);

    Page<Post> findByConditions(String condition, String search);

    Boolean passwordCheck(Long id, String password);

    Boolean isSecretPost(Long id);

    List<Post> findAll(int page, String code);

    Optional<Post> findPrevPost(Long id);

    Optional<Post> findNextPost(Long id);
}

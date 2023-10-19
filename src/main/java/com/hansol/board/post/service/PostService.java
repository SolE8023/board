package com.hansol.board.post.service;

import com.hansol.board.post.domain.Post;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostService {
    Optional<Post> findPostById(Long id);

    Page<Post> findListOrderby(int page, String code);

    Post savePost(Post post);

    Optional<Post> findSecretPostById(Long id, String password);

    Post prevPost(Long id);

    Post nextPost(Long id);

    void remove(Long id, String password);
}

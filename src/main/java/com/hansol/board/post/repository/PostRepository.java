package com.hansol.board.post.repository;

import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    PostEntity findById(Long postId);

    PostEntity save(PostEntity entity);

    PostEntity update(PostEntity post);

    PostEntity findByIdAndAndPassword(Long id, String password);

    Page<PostEntity> findListOrderby(int page, String code);

    void remove(Long id, String password);

    Page<PostEntity> findByConditions(String condition, String search);

    Boolean passwordCheck(Long id, String password);

    Boolean isSecretPost(Long id);

    List<PostEntity> findAll(int page, String code);

    PostEntity findPrevPost(Long id);

    PostEntity findNextPost(Long id);

    Optional<Post> checkPassword(Long id, String password);
}

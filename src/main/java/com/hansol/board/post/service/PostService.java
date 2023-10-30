package com.hansol.board.post.service;

import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.request.CheckPassword;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostEntity findPostById(Long id);

    Page<PostEntity> findListOrderby(int page, String code);

    PostEntity savePost(PostEntity entity, List<MultipartFile> files);

    PostEntity update(PostEntity post, List<MultipartFile> files);

    PostEntity findSecretPostById(Long id, String password);

    PostEntity prevPost(Long id);

    PostEntity nextPost(Long id);

    void remove(Long id, String password);

    Boolean checkPassword(CheckPassword request);
}

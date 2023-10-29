package com.hansol.board.post.service;

import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.repository.PostRepository;
import com.hansol.board.post.request.CheckPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostEntity findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Page<PostEntity> findListOrderby(int page, String code) {
        return postRepository.findListOrderby(page, code);
    }

    @Override
    public PostEntity savePost(PostEntity entity) {
        return postRepository.save(entity);
    }

    @Override
    public PostEntity update(PostEntity post) {
        return postRepository.update(post);
    }

    @Override
    public PostEntity findSecretPostById(Long id, String password) {
        if (postRepository.isSecretPost(id)) {
            if (postRepository.passwordCheck(id, password)) {
                return postRepository.findById(id);
            } else {
                throw new PasswordErrorException();
            }
        } else {
            return postRepository.findById(id);
        }
    }

    @Override
    public PostEntity prevPost(Long id) {
        return postRepository.findPrevPost(id);
    }

    @Override
    public PostEntity nextPost(Long id) {
        return postRepository.findNextPost(id);
    }

    @Override
    public void remove(Long id, String password) {
        postRepository.remove(id, password);
    }

    @Override
    public Boolean checkPassword(CheckPassword request) {
        Optional<Post> findPost = postRepository.checkPassword(request.getId(), request.getPassword());
        return findPost.isPresent();
    }
}

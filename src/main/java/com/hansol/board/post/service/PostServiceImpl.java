package com.hansol.board.post.service;

import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Page<Post> findListOrderby(int page, String code) {
        return postRepository.findListOrderby(page, code);
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findSecretPostById(Long id, String password) {
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
    public Post prevPost(Long id) {
        return postRepository.findPrevPost(id).orElseThrow(NoPostException::new);
    }

    @Override
    public Post nextPost(Long id) {
        return postRepository.findNextPost(id).orElseThrow(NoPostException::new);
    }

    @Override
    public void remove(Long id, String password) {
        postRepository.remove(id, password);
    }


}

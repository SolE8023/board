package com.hansol.board.mock;

import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.repository.PostRepository;
import com.hansol.board.post.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FakePostService implements PostService {

    private final PostRepository postRepository;

    @Override
    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findOrderByNotice();
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
        Optional<Post> post = findPostById(id);
        if (post.isPresent()) {
            List<Post> posts = postRepository.findAll();
            int index = posts.indexOf(post.get());
            for (int i = index-1; i >= 0; i--) {
                if(!posts.get(i).getSecret()) return posts.get(i);
            }
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }
        return null;
    }

    @Override
    public Post nextPost(Long id) {
        Optional<Post> post = findPostById(id);
        if (post.isPresent()) {
            List<Post> posts = postRepository.findAll();
            int index = posts.indexOf(post.get());
            for (int i = index+1; i < posts.size(); i++) {
                if(!posts.get(i).getSecret()) return posts.get(i);
            }
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }
        return null;
    }

    @Override
    public void remove(Long id, String password) {
        postRepository.remove(id, password);
    }


}

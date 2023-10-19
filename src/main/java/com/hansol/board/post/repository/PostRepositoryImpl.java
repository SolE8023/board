package com.hansol.board.post.repository;

import com.hansol.board.common.PageSetting;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository{
    private final PostJpaRepository postJpaRepository;
    @Override
    public Optional<Post> findById(Long postId) {
        Optional<PostEntity> findPost = postJpaRepository.findById(postId);
        if (findPost.isPresent()) {
            return Optional.ofNullable(Post.from(findPost.get()));
        } else {
            throw new NoPostException();
        }
    }

    @Override
    public Post save(Post post) {
        PostEntity saved = postJpaRepository.save(PostEntity.from(post));
        return Post.from(saved);
    }

    @Override
    @Transactional
    public Post update(Post post, String password) {
        Optional<PostEntity> findPost = postJpaRepository.findByIdAndPassword(post.getId(), password);
        findPost.ifPresent(p -> {
            p.setTitle(post.getTitle());
            p.setWriter(post.getWriter().getName());
            p.setContent(post.getContent());
            p.setSecret(post.getSecret());
            p.setNotice(post.getNotice());
            p.setPassword(post.getPassword());
        });

        findPost.orElseThrow(NoPostException::new);

        return Post.from(findPost.get());
    }

    @Override
    public Optional<Post> findByIdAndAndPassword(Long id, String password) {
        Optional<PostEntity> findPost = postJpaRepository.findByIdAndPassword(id, password);
        findPost.orElseThrow(NoPostException::new);
        return Optional.ofNullable(Post.from(findPost.get()));
    }

    @Override
    public Page<Post> findListOrderby(int page, String code) {
        Pageable pageable = PageSetting.getPostPageable(page);
        Page<PostEntity> list = postJpaRepository.findListOrderby(code, pageable);
        return list.map(Post::from);
    }

    @Override
    public void remove(Long id, String password) {

    }

    @Override
    public List<Post> findByConditions(String condition, String search) {
        return null;
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        return null;
    }

    @Override
    public List<Post> findOrderByCreatedAt() {
        return null;
    }

    @Override
    public Boolean isSecretPost(Long id) {
        return null;
    }

    @Override
    public Page<Post> findAll(int page, String code) {
        return null;
    }

    @Override
    public Optional<Post> findPrevPost(Long id) {
        Long prevId = postJpaRepository.findPrevId(id);
        return findById(prevId);
    }

    @Override
    public Optional<Post> findNextPost(Long id) {
        Long nextId = postJpaRepository.findNextId(id);
        return findById(nextId);
    }
}

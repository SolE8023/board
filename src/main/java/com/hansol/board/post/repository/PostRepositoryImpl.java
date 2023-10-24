package com.hansol.board.post.repository;

import com.hansol.board.common.PageSetting;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
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
    private final PostJpaRepository jpaRepository;
    @Override
    public Optional<Post> findById(Long postId) {
        Optional<PostEntity> findPost = jpaRepository.findById(postId);
        if (findPost.isPresent()) {
            return Optional.ofNullable(Post.fromEntity(findPost.get()));
        } else {
            throw new NoPostException();
        }
    }

    @Override
    public Post save(Post post) {
        PostEntity saved = jpaRepository.save(PostEntity.from(post));
        return Post.fromEntity(saved);
    }

    @Override
    @Transactional
    public Post update(Post post) {
        Optional<PostEntity> findPost = jpaRepository.findById(post.getId());
        findPost.ifPresent(p -> {
            p.setTitle(post.getTitle());
            p.setWriter(post.getWriter());
            p.setContent(post.getContent());
            p.setSecret(post.getSecret());
            p.setNotice(post.getNotice());
            p.setPassword(post.getPassword());
        });

        findPost.orElseThrow(NoPostException::new);

        return Post.fromEntity(findPost.get());
    }

    @Override
    public Optional<Post> findByIdAndAndPassword(Long id, String password) {
        Optional<PostEntity> findPost = jpaRepository.findByIdAndPassword(id, password);
        findPost.orElseThrow(NoPostException::new);
        return Optional.ofNullable(Post.fromEntity(findPost.get()));
    }

    @Override
    public Page<Post> findListOrderby(int page, String code) {
        Pageable pageable = PageSetting.getPostPageable(page);
        Page<PostEntity> list = jpaRepository.findListOrderby(code, pageable);
        return list.map(Post::fromEntity);
    }

    @Override
    public void remove(Long id, String password) {
        if (passwordCheck(id, password)) {
            jpaRepository.deleteById(id);
        } else {
            throw new PasswordErrorException();
        }
    }

    @Override
    public Page<Post> findByConditions(String condition, String search) {
        //querydsl로 해결해 보기
        return null;
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        Optional<PostEntity> findPost = jpaRepository.findByIdAndPassword(id, password);
        return findPost.isPresent();

    }

    @Override
    public Boolean isSecretPost(Long id) {
        Optional<PostEntity> findPost = jpaRepository.findById(id);
        if (findPost.isPresent()) {
            return findPost.get().getSecret();
        } else {
            throw new NoPostException();
        }
    }

    @Override
    public List<Post> findAll(int page, String code) {
        Pageable pageable = PageSetting.getPostPageable(page);
        Page<PostEntity> posts = jpaRepository.findListOrderby(code, pageable);
        return posts.stream().map(Post::fromEntity).toList();
    }

    @Override
    public Optional<Post> findPrevPost(Long id) {
        Long prevId = jpaRepository.findPrevId(id);
        return findById(prevId);
    }

    @Override
    public Optional<Post> findNextPost(Long id) {
        Long nextId = jpaRepository.findNextId(id);
        return findById(nextId);
    }

    @Override
    public Optional<Post> checkPassword(Long id, String password) {
        Optional<PostEntity> findPost = jpaRepository.findByIdAndPassword(id, password);
        return findPost.map(Post::fromEntity).or(Optional::empty);
    }
}

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
    public PostEntity findById(Long postId) {
        Optional<PostEntity> findPost = jpaRepository.findById(postId);
        return findPost.orElseThrow(NoPostException::new);
    }

    @Override
    public PostEntity save(PostEntity entity) {
        return jpaRepository.save(entity);
    }

    @Override
    @Transactional
    public PostEntity update(PostEntity post) {
        Optional<PostEntity> findPost = jpaRepository.findById(post.getId());
        findPost.ifPresent(p -> {
            p.setTitle(post.getTitle());
            p.setWriter(post.getWriter());
            p.setContent(post.getContent());
            p.setSecret(post.getSecret());
            p.setNotice(post.getNotice());
            p.setPassword(post.getPassword());
        });

        return findPost.orElseThrow(NoPostException::new);
    }

    @Override
    public PostEntity findByIdAndAndPassword(Long id, String password) {
        Optional<PostEntity> findPost = jpaRepository.findByIdAndPassword(id, password);
        return findPost.orElseThrow(NoPostException::new);
    }

    @Override
    public Page<PostEntity> findListOrderby(int page, String code) {
        Pageable pageable = PageSetting.getPostPageable(page);
        if (code.equals("gallery")) {
            pageable = PageSetting.getGalleryPageable(page);
        }

        return jpaRepository.findByCodeAndParentPostIsNull(code, pageable);
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
    public Page<PostEntity> findByConditions(String condition, String search) {
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
    public List<PostEntity> findAll(int page, String code) {
        Pageable pageable = PageSetting.getPostPageable(page);
        Page<PostEntity> posts = jpaRepository.findByCodeAndParentPostIsNull(code, pageable);
        return posts.stream().toList();
    }

    @Override
    public PostEntity findPrevPost(Long id) {
        Long prevId = jpaRepository.findPrevId(id);
        return findById(prevId);
    }

    @Override
    public PostEntity findNextPost(Long id) {
        Long nextId = jpaRepository.findNextId(id);
        return findById(nextId);
    }

    @Override
    public Optional<Post> checkPassword(Long id, String password) {
        Optional<PostEntity> findPost = jpaRepository.findByIdAndPassword(id, password);
        return findPost.map(Post::fromEntity).or(Optional::empty);
    }
}

package com.hansol.board.comment.repository;

import com.hansol.board.comment.domain.CommentEntity;
import com.hansol.board.exception.NoCommentException;
import com.hansol.board.exception.PasswordErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final CommentJpaRepository jpaRepository;

    @Override
    public CommentEntity save(CommentEntity comment) {
        return jpaRepository.save(comment);
    }

    @Override
    @Transactional
    public CommentEntity update(CommentEntity comment) {
        Optional<CommentEntity> findComment = jpaRepository.findById(comment.getId());
        CommentEntity find = findComment.orElseThrow(NoCommentException::new);
        find.setWriter(comment.getWriter());
        find.setSecret(comment.getSecret());
        find.setPassword(comment.getPassword());
        find.setContent(comment.getContent());
        return find;
    }

    @Override
    public Page<CommentEntity> findByPostId(Long postId, Pageable pageable) {
        return jpaRepository.findByPostId(postId, pageable);
    }

    @Override
    public Optional<CommentEntity> findByIdAndPassword(Long id, String password) {
        return jpaRepository.findByIdAndPassword(id, password);
    }

    @Override
    public Optional<CommentEntity> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        Optional<CommentEntity> findComment = findByIdAndPassword(id, password);
        return findComment.isPresent();
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
}

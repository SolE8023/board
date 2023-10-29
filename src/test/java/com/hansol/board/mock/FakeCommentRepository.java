package com.hansol.board.mock;

import com.hansol.board.comment.domain.CommentEntity;
import com.hansol.board.comment.repository.CommentRepository;
import com.hansol.board.exception.PasswordErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeCommentRepository implements CommentRepository {
    private Long id = 0L;
    private final List<CommentEntity> comments = new ArrayList<>();
    @Override
    public CommentEntity save(CommentEntity comment) {
        if (comment.getId() == null || comment.getId().equals(0L)) {
            comment.setId(++id);
            comments.add(comment);
        } else {
            throw new IllegalArgumentException("잘못된 commentId 입니다.");
        }
        return comment;
    }

    @Override
    public CommentEntity update(CommentEntity comment) {
        comments.removeIf(c -> c.getId().equals(comment.getId()));
        comments.add(comment);
        return comment;
    }

    @Override
    public Page<CommentEntity> findByPostId(Long id, Pageable pageable) {
        List<CommentEntity> result = new ArrayList<>();
        for (CommentEntity comment : comments) {
            if(comment.getSecret()) comment.setContent("비밀글 입니다.");
            result.add(comment);
        }

        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Optional<CommentEntity> findByIdAndPassword(Long id, String password) {
        return Optional.ofNullable(comments.stream()
                .filter(c -> c.getId().equals(id) && c.getPassword().equals(password))
                .findAny()
                .orElseThrow(PasswordErrorException::new));
    }

    @Override
    public Optional<CommentEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        for (CommentEntity comment : comments) {
            if (comment.getId().equals(id) && comment.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        comments.removeIf(c -> c.getId().equals(id));
    }
}

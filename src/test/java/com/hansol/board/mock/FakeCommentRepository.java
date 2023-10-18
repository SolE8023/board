package com.hansol.board.mock;

import com.hansol.board.comment.domain.Comment;
import com.hansol.board.comment.repository.CommentRepository;
import com.hansol.board.exception.PasswordErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeCommentRepository implements CommentRepository {
    private Long id = 0L;
    private final List<Comment> comments = new ArrayList<>();
    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null || comment.getId().equals(0L)) {
            comment.setId(++id);
            comments.add(comment);
        } else {
            comments.removeIf(c -> c.getId().equals(comment.getId()));
            comments.add(comment);
        }
        return comment;
    }

    @Override
    public List<Comment> findByPostId(Long id) {
        List<Comment> result = new ArrayList<>();
        for (Comment comment : comments) {
            if(comment.getSecret()) comment.setContent("비밀글 입니다.");
            result.add(comment);
        }
        return result;
    }

    @Override
    public Optional<Comment> findByIdAndPassword(Long id, String password) {
        return Optional.ofNullable(comments.stream()
                .filter(c -> c.getId().equals(id) && c.getPassword().equals(password))
                .findAny()
                .orElseThrow(PasswordErrorException::new));
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        for (Comment comment : comments) {
            if (comment.getId().equals(id) && comment.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove(Long id, String password) {
        if (passwordCheck(id, password)) {
            comments.removeIf(c -> c.getId().equals(id) && c.getPassword().equals(password));
        } else {
            throw new PasswordErrorException();
        }
    }
}

package com.hansol.board.common.api;

import com.hansol.board.comment.domain.CommentEntity;
import com.hansol.board.comment.form.EditCommentForm;
import com.hansol.board.comment.form.SaveCommentForm;
import com.hansol.board.comment.repository.CommentRepository;
import com.hansol.board.comment.request.CheckPassword;
import com.hansol.board.comment.response.EditFormResponse;
import com.hansol.board.exception.NoAuthException;
import com.hansol.board.exception.NoCommentException;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentApiV1 {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @PostMapping
    public Object addComment(@RequestBody @Validated SaveCommentForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        PostEntity entity = postRepository.findById(form.getPostId());
        CommentEntity saved = commentRepository.save(CommentEntity.fromSaveForm(form, entity));
        if (saved.getId() != null) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 입니다.");
        }
    }

    @PostMapping("comment-check-password")
    public Object checkPassword(@RequestBody @Validated CheckPassword request,
                                BindingResult bindingResult,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        Optional<CommentEntity> findComment =
                commentRepository.findByIdAndPassword(request.getId(), request.getPassword());

        if (findComment.isPresent()) {
            session.setAttribute("commentAuth", true);
            session.setAttribute("commentType", request.getType());
            session.setAttribute("commentId", findComment.get().getId());
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 비밀번호 입니다.");
        }
    }

    @GetMapping("get-edit-data")
    public EditFormResponse getEditData(@RequestParam Long id,
                                        HttpSession session) {
        Boolean auth = (Boolean) session.getAttribute("commentAuth");
        String type = (String) session.getAttribute("commentType");
        Long commentId = (Long) session.getAttribute("commentId");
        if (auth && type.equals("edit") && commentId.equals(id)) {
            Optional<CommentEntity> findComment = commentRepository.findById(id);
            CommentEntity comment = findComment.orElseThrow(NoCommentException::new);
            return EditFormResponse.fromEntity(comment);
        } else {
            throw new NoCommentException();
        }
    }

    @PatchMapping
    public Object edit(@RequestBody @Validated EditCommentForm form,
                        BindingResult bindingResult,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }
        Boolean auth = (Boolean) session.getAttribute("commentAuth");
        String type = (String) session.getAttribute("commentType");
        Long id = (Long) session.getAttribute("commentId");

        if (auth && type.equals("edit") && id.equals(form.getId())) {
            CommentEntity entity = CommentEntity.fromEditForm(form);
            commentRepository.update(entity);
            return ResponseEntity.ok().body("success");
        } else {
            throw new NoAuthException();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam Long id,
                                         HttpSession session) {
        Boolean auth = (Boolean) session.getAttribute("commentAuth");
        String type = (String) session.getAttribute("commentType");
        Long commentId = (Long) session.getAttribute("commentId");
        if (auth && type.equals("delete") && commentId.equals(id)) {
            commentRepository.delete(id);
            return ResponseEntity.ok().body("success");
        } else {
            throw new NoAuthException();
        }
    }
}

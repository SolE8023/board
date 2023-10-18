package com.hansol.board.comment;

import com.hansol.board.comment.domain.Comment;
import com.hansol.board.common.domain.Writer;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.mock.TestContainer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentRepositoryTest {
    @Test
    void 댓글을_등록할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .password("qwer1234")
                .build();

        //when
        Comment saved = testContainer.commentRepository.save(comment);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 댓글이_비밀글일_경우_내용이_보이지_않아야_한다() {
        //given
        Comment comment1 = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .secret(true)
                .build();
        Comment comment2 = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .secret(false)
                .build();
        TestContainer testContainer = TestContainer.builder().build();

        testContainer.commentRepository.save(comment1);
        testContainer.commentRepository.save(comment2);

        //when
        List<Comment> comments = testContainer.commentRepository.findByPostId(1L);

        //then
        assertThat(comments.get(0).getContent()).isEqualTo("비밀글 입니다.");
        assertThat(comments.get(1).getContent()).isEqualTo("댓글1빠");
    }

    @Test
    void 비밀댓글을_보려면_비밀번호를_올바르게_입력해야_한다() {
        //given
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .secret(true)
                .password("123123")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        Comment saved = testContainer.commentRepository.save(comment);

        //when
        String rightPassword = "123123";
        String wrongPassword = "111111";
        Optional<Comment> pass = testContainer.commentRepository.findByIdAndPassword(saved.getId(), rightPassword);

        //then
        assertThat(pass.isPresent()).isTrue();
        assertThatThrownBy(() -> testContainer.commentRepository.findByIdAndPassword(saved.getId(), wrongPassword))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 댓글을_수정할_수_있다() {
        //given
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .password("123123")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        Comment saved = testContainer.commentRepository.save(comment);

        //when
        Comment update = Comment.builder()
                .id(saved.getId())
                .createdAt(LocalDateTime.of(2023,10,15,13,15))
                .writer(Writer.builder().name("황진이").build())
                .postId(1L)
                .content("댓글2빠")
                .password("321321")
                .build();
        Comment updated = testContainer.commentRepository.update(update, "123123");

        //then
        Optional<Comment> findComment = testContainer.commentRepository.findByIdAndPassword(updated.getId(), updated.getPassword());
        assertThat(findComment.isPresent()).isTrue();
        assertThat(findComment.get().getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 15, 13, 15));
        assertThat(findComment.get().getWriter().getName()).isEqualTo("황진이");
        assertThat(findComment.get().getContent()).isEqualTo("댓글2빠");
        assertThat(findComment.get().getPassword()).isEqualTo("321321");
    }

    @Test
    void 비밀번호가_일치할_시_댓글_하나의_데이터를_가져올_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .password("qwer1234")
                .build();
        Comment saved = testContainer.commentRepository.save(comment);

        //when
        Optional<Comment> findComment = testContainer.commentRepository.findByIdAndPassword(saved.getId(), saved.getPassword());

        //then
        assertThat(findComment.isPresent()).isTrue();
        assertThat(findComment.get().getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 15, 13, 12));
        assertThat(findComment.get().getWriter().getName()).isEqualTo("홍길동");
        assertThat(findComment.get().getContent()).isEqualTo("댓글1빠");
    }

    @Test
    void 댓글을_삭제시_비밀번호가_일치해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .password("qwer1234")
                .build();
        Comment saved = testContainer.commentRepository.save(comment);

        //when
        testContainer.commentRepository.remove(saved.getId(), saved.getPassword());

        //then
        assertThatThrownBy(() -> testContainer.commentRepository.findByIdAndPassword(saved.getId(), saved.getPassword()))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 댓글_삭제시_비밀번호가_일치하지_않으면_예외를_던진다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .password("qwer1234")
                .build();
        Comment saved = testContainer.commentRepository.save(comment);

        //when
        //then
        assertThatThrownBy(() -> testContainer.commentRepository.remove(saved.getId(), "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 비밀댓글_조회시_비밀번호가_일치하지_않으면_예외를_던진다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Comment comment = Comment.builder()
                .createdAt(LocalDateTime.of(2023,10,15,13,12))
                .writer(Writer.builder().name("홍길동").build())
                .postId(1L)
                .content("댓글1빠")
                .password("qwer1234")
                .secret(true)
                .build();
        Comment saved = testContainer.commentRepository.save(comment);

        //when
        //then
        assertThatThrownBy(() -> testContainer.commentRepository.findByIdAndPassword(saved.getId(), "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }
    
}

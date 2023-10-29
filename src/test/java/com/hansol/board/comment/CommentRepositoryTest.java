package com.hansol.board.comment;

import com.hansol.board.comment.domain.CommentEntity;
import com.hansol.board.common.PageSetting;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.mock.TestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentRepositoryTest {
    @Test
    void 댓글을_등록할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        CommentEntity comment = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .password("qwer1234")
                .build();

        //when
        CommentEntity saved = testContainer.commentRepository.save(comment);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 댓글이_비밀글일_경우_내용이_보이지_않아야_한다() {
        //given
        CommentEntity comment1 = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .secret(true)
                .build();
        CommentEntity comment2 = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .secret(false)
                .build();
        TestContainer testContainer = TestContainer.builder().build();

        //when
        testContainer.commentRepository.save(comment1);
        testContainer.commentRepository.save(comment2);

        Pageable pageable = PageSetting.getCommentPageable(0);

        Page<CommentEntity> comments = testContainer.commentRepository.findByPostId(1L, pageable);

        //then
        assertThat(comments.getContent().get(0).getContent()).isEqualTo("비밀글 입니다.");
        assertThat(comments.getContent().get(1).getContent()).isEqualTo("댓글1빠");
    }

    @Test
    void 비밀댓글을_보려면_비밀번호를_올바르게_입력해야_한다() {
        //given
        CommentEntity comment = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .secret(true)
                .password("123123")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        CommentEntity saved = testContainer.commentRepository.save(comment);

        //when
        String rightPassword = "123123";
        String wrongPassword = "111111";
        Optional<CommentEntity> pass = testContainer.commentRepository.findByIdAndPassword(saved.getId(), rightPassword);

        //then
        assertThat(pass.isPresent()).isTrue();
        assertThatThrownBy(() -> testContainer.commentRepository.findByIdAndPassword(saved.getId(), wrongPassword))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 댓글을_수정할_수_있다() {
        //given
        CommentEntity comment = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .password("123123")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        CommentEntity saved = testContainer.commentRepository.save(comment);

        //when
        CommentEntity update = CommentEntity.builder()
                .id(saved.getId())
                .writer("황진이")
                .content("댓글2빠")
                .password("321321")
                .build();
        CommentEntity updated = testContainer.commentRepository.update(update);

        //then
        Optional<CommentEntity> findComment = testContainer.commentRepository.findByIdAndPassword(updated.getId(), updated.getPassword());
        assertThat(findComment.isPresent()).isTrue();
        assertThat(findComment.get().getWriter()).isEqualTo("황진이");
        assertThat(findComment.get().getContent()).isEqualTo("댓글2빠");
        assertThat(findComment.get().getPassword()).isEqualTo("321321");
    }

    @Test
    void 비밀번호가_일치할_시_댓글_하나의_데이터를_가져올_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        CommentEntity comment = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .password("qwer1234")
                .build();
        CommentEntity saved = testContainer.commentRepository.save(comment);

        //when
        Optional<CommentEntity> findComment = testContainer.commentRepository.findByIdAndPassword(saved.getId(), saved.getPassword());

        //then
        assertThat(findComment.isPresent()).isTrue();
        assertThat(findComment.get().getWriter()).isEqualTo("홍길동");
        assertThat(findComment.get().getContent()).isEqualTo("댓글1빠");
    }

    @Test
    void 댓글을_삭제시_비밀번호가_일치해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        CommentEntity comment = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .password("qwer1234")
                .build();
        CommentEntity saved = testContainer.commentRepository.save(comment);

        //when
        testContainer.commentRepository.delete(saved.getId());

        //then
        assertThatThrownBy(() -> testContainer.commentRepository.findByIdAndPassword(saved.getId(), saved.getPassword()))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 비밀댓글_조회시_비밀번호가_일치하지_않으면_예외를_던진다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        CommentEntity comment = CommentEntity.builder()
                .writer("홍길동")
                .content("댓글1빠")
                .password("qwer1234")
                .secret(true)
                .build();
        CommentEntity saved = testContainer.commentRepository.save(comment);

        //when
        //then
        assertThatThrownBy(() -> testContainer.commentRepository.findByIdAndPassword(saved.getId(), "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }
    
}

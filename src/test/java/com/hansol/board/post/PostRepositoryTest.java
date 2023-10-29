package com.hansol.board.post;

import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.mock.TestContainer;
import com.hansol.board.post.domain.Post;
import com.hansol.board.common.domain.Writer;
import com.hansol.board.post.domain.PostEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class PostRepositoryTest {
    @Test
    void 게시글_하나를_조회할_수_있다() {
        //given
        PostEntity post = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(false)
                .notice(false)
                .build();

        TestContainer testContainer = TestContainer.builder().build();
        PostEntity saved = testContainer.postRepository.save(post);

        //when
        PostEntity findPost = testContainer.postRepository.findById(saved.getId());

        //then
        assertThat(findPost).isNotNull();
    }

    @Test
    void 게시글이_없을_경우_조회가_안된다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();

        //when
        //then
        assertThatThrownBy(() -> testContainer.postRepository.findById(1L))
                .isInstanceOf(NoPostException.class);
    }

    @Test
    void 비밀글일_경우_올바른_비밀번호를_입력해야_한다() {
        //given
        PostEntity post = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(true)
                .notice(false)
                .password("qwer1234")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.postRepository.save(post);

        //when
        PostEntity findPost = testContainer.postRepository.findByIdAndAndPassword(1L, "qwer1234");

        //then
        assertThat(findPost).isNotNull();
        assertThatThrownBy(() -> testContainer.postRepository.findByIdAndAndPassword(1L, "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 비밀글일_경우_잘못된_비밀번호를_입력하면_안된다() {
        //given
        PostEntity post = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(true)
                .notice(false)
                .password("qwer1234")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.postRepository.save(post);

        //when
        assertThatThrownBy(() -> testContainer.postRepository.findByIdAndAndPassword(1L, "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 공지사항일_경우_제일_먼저_노출되어야_한다() {
        //given
        PostEntity post1 = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("게시글 제목2")
                .writer("황진이")
                .content("게시글 내용2")
                .secret(false)
                .notice(true)
                .password("qwer1234")
                .build();

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);

        Page<PostEntity> posts = testContainer.postRepository.findListOrderby(0,"notice");
        assertThat(posts.getContent().get(0).getId()).isEqualTo(2L);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(1L);
    }

    @Test
    void 게시글을_수정할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();

        PostEntity savePost = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();


        //when
        PostEntity saved = testContainer.postRepository.save(savePost);
        PostEntity updatePost = PostEntity.builder()
                .id(saved.getId())
                .title("게시글 제목2")
                .writer("황진이")
                .content("게시글 내용2")
                .secret(false)
                .notice(false)
                .password("123123")
                .build();
        PostEntity updated = testContainer.postRepository.update(updatePost);

        //then
        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getTitle()).isEqualTo("게시글 제목2");
        assertThat(updated.getWriter()).isEqualTo("황진이");
        assertThat(updated.getContent()).isEqualTo("게시글 내용2");

    }

    @Test
    void 비밀번호가_일치할_경우_게시글을_삭제할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();
        PostEntity saved = testContainer.postRepository.save(post);
        Long savedId = saved.getId();

        PostEntity findPost = testContainer.postRepository.findById(savedId);
        assertThat(findPost).isNotNull();

        //when
        testContainer.postRepository.remove(saved.getId(), saved.getPassword());

        //then
        assertThatThrownBy(() -> testContainer.postRepository.findById(savedId))
                .isInstanceOf(NoPostException.class);
    }

    @Test
    void 게시글을_검색할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();

        PostEntity post1 = PostEntity.builder()
                .title("게시글 제목1")
                .writer("홍길동")
                .content("게시글 내용1")
                .secret(false)
                .notice(false)
                .build();

        //when
        PostEntity post2 = PostEntity.builder()
                .title("게시글 제목2")
                .writer("황진이")
                .content("게시글 내용2")
                .secret(false)
                .notice(false)
                .build();

        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);

        Page<PostEntity> findByTitle1 = testContainer.postRepository.findByConditions("title", "게시글 제목");
        Page<PostEntity> findByTitle2 = testContainer.postRepository.findByConditions("title", "1");
        Page<PostEntity> findByTitle3 = testContainer.postRepository.findByConditions("title", "2");
        Page<PostEntity> findByWriter1 = testContainer.postRepository.findByConditions("writer", "홍길동");
        Page<PostEntity> findByWriter2 = testContainer.postRepository.findByConditions("writer", "황진이");
        Page<PostEntity> findByContent1 = testContainer.postRepository.findByConditions("content", "게시글 내용");
        Page<PostEntity> findByContent2 = testContainer.postRepository.findByConditions("content", "1");
        Page<PostEntity> findByContent3 = testContainer.postRepository.findByConditions("content", "2");
        Page<PostEntity> findByAllCondition1 = testContainer.postRepository.findByConditions("all", "2");
        Page<PostEntity> findByAllCondition2 = testContainer.postRepository.findByConditions("all", "게시글");
        Page<PostEntity> findByAllCondition3 = testContainer.postRepository.findByConditions("all", "황진이");

        assertThat(findByTitle1.getContent().size()).isEqualTo(2);
        assertThat(findByTitle2.getContent().size()).isEqualTo(1);
        assertThat(findByTitle3.getContent().size()).isEqualTo(1);
        assertThat(findByWriter1.getContent().size()).isEqualTo(1);
        assertThat(findByWriter2.getContent().size()).isEqualTo(1);
        assertThat(findByContent1.getContent().size()).isEqualTo(2);
        assertThat(findByContent2.getContent().size()).isEqualTo(1);
        assertThat(findByContent3.getContent().size()).isEqualTo(1);
        assertThat(findByAllCondition1.getContent().size()).isEqualTo(1);
        assertThat(findByAllCondition2.getContent().size()).isEqualTo(2);
        assertThat(findByAllCondition3.getContent().size()).isEqualTo(1);
    }
}

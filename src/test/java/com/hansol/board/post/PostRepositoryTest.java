package com.hansol.board.post;

import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.mock.TestContainer;
import com.hansol.board.post.domain.Post;
import com.hansol.board.common.domain.Writer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostRepositoryTest {
    @Test
    void 게시글_하나를_조회할_수_있다() {
        //given
        Post post = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .build();

        TestContainer testContainer = TestContainer.builder().build();
        Post saved = testContainer.postRepository.save(post);

        //when
        Optional<Post> findPost = testContainer.postRepository.findById(saved.getId());

        //then
        assertThat(findPost.isPresent()).isTrue();
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
        Post post = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(true)
                .notice(false)
                .password("qwer1234")
                .build();
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.postRepository.save(post);

        //when
        Optional<Post> findPost = testContainer.postRepository.findByIdAndAndPassword(1L, "qwer1234");

        //then
        assertThat(findPost.isPresent()).isTrue();
        assertThatThrownBy(() -> testContainer.postRepository.findByIdAndAndPassword(1L, "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 비밀글일_경우_잘못된_비밀번호를_입력하면_안된다() {
        //given
        Post post = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
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
        Post post1 = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();
        Post post2 = Post.builder()
                .title("게시글 제목2")
                .writer(Writer.builder().name("황진이").build())
                .content("게시글 내용2")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 31))
                .updatedAt(null)
                .secret(false)
                .notice(true)
                .password("qwer1234")
                .build();

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);

        List<Post> posts = testContainer.postRepository.findOrderByNotice();
        assertThat(posts.get(0).getId()).isEqualTo(2L);
        assertThat(posts.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void 공지사항이_아닐_경우_생성일_기준으로_정렬되어야_한다() {
        //given
        Post post1 = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();
        Post post2 = Post.builder()
                .title("게시글 제목2")
                .writer(Writer.builder().name("황진이").build())
                .content("게시글 내용2")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 31))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);

        List<Post> posts = testContainer.postRepository.findOrderByCreatedAt();
        assertThat(posts.get(0).getId()).isEqualTo(1L);
        assertThat(posts.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void 게시글을_수정할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();

        Post savePost = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();


        //when
        Post saved = testContainer.postRepository.save(savePost);
        Post updatePost = Post.builder()
                .id(saved.getId())
                .title("게시글 제목2")
                .writer(Writer.builder().name("황진이").build())
                .content("게시글 내용2")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 31))
                .updatedAt(LocalDateTime.of(2023, 10, 17, 15, 32))
                .secret(false)
                .notice(false)
                .password("123123")
                .build();
        Post updated = testContainer.postRepository.update(updatePost, "qwer1234");

        //then
        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getTitle()).isEqualTo("게시글 제목2");
        assertThat(updated.getWriter().getName()).isEqualTo("황진이");
        assertThat(updated.getContent()).isEqualTo("게시글 내용2");
        assertThat(updated.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 17, 15, 31));
        assertThat(updated.getUpdatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 17, 15, 32));

    }

    @Test
    void 비밀번호가_일치할_경우_게시글을_삭제할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .password("qwer1234")
                .build();
        Post saved = testContainer.postRepository.save(post);
        Long savedId = saved.getId();

        Optional<Post> findPost = testContainer.postRepository.findById(savedId);
        assertThat(findPost.isPresent()).isTrue();

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

        Post post1 = Post.builder()
                .title("게시글 제목1")
                .writer(Writer.builder().name("홍길동").build())
                .content("게시글 내용1")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 30))
                .updatedAt(null)
                .secret(false)
                .notice(false)
                .build();

        //when
        Post post2 = Post.builder()
                .title("게시글 제목2")
                .writer(Writer.builder().name("황진이").build())
                .content("게시글 내용2")
                .createdAt(LocalDateTime.of(2023, 10, 17, 15, 31))
                .updatedAt(LocalDateTime.of(2023, 10, 17, 15, 31))
                .secret(false)
                .notice(false)
                .build();

        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);

        List<Post> findByTitle1 = testContainer.postRepository.findByConditions("title", "게시글 제목");
        List<Post> findByTitle2 = testContainer.postRepository.findByConditions("title", "1");
        List<Post> findByTitle3 = testContainer.postRepository.findByConditions("title", "2");
        List<Post> findByWriter1 = testContainer.postRepository.findByConditions("writer", "홍길동");
        List<Post> findByWriter2 = testContainer.postRepository.findByConditions("writer", "황진이");
        List<Post> findByContent1 = testContainer.postRepository.findByConditions("content", "게시글 내용");
        List<Post> findByContent2 = testContainer.postRepository.findByConditions("content", "1");
        List<Post> findByContent3 = testContainer.postRepository.findByConditions("content", "2");
        List<Post> findByAllCondition1 = testContainer.postRepository.findByConditions("all", "2");
        List<Post> findByAllCondition2 = testContainer.postRepository.findByConditions("all", "게시글");
        List<Post> findByAllCondition3 = testContainer.postRepository.findByConditions("all", "황진이");

        assertThat(findByTitle1.size()).isEqualTo(2);
        assertThat(findByTitle2.size()).isEqualTo(1);
        assertThat(findByTitle3.size()).isEqualTo(1);
        assertThat(findByWriter1.size()).isEqualTo(1);
        assertThat(findByWriter2.size()).isEqualTo(1);
        assertThat(findByContent1.size()).isEqualTo(2);
        assertThat(findByContent2.size()).isEqualTo(1);
        assertThat(findByContent3.size()).isEqualTo(1);
        assertThat(findByAllCondition1.size()).isEqualTo(1);
        assertThat(findByAllCondition2.size()).isEqualTo(2);
        assertThat(findByAllCondition3.size()).isEqualTo(1);
    }
}

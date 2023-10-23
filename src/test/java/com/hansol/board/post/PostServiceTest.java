package com.hansol.board.post;

import com.hansol.board.common.domain.Writer;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.mock.TestContainer;
import com.hansol.board.post.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class PostServiceTest {
    @Test
    void 게시글_저장이_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();

        //when
        Post saved = testContainer.postService.savePost(post);

        //then
        assertThat(saved.getTitle()).isEqualTo("제목1");
    }
    @Test
    void 게시글_한_건을_조회할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post saved = testContainer.postService.savePost(post);

        //when
        Optional<Post> findPost = testContainer.postService.findPostById(saved.getId());

        //then
        assertThat(findPost.isPresent()).isTrue();
        assertThat(findPost.get().getTitle()).isEqualTo(saved.getTitle());
        assertThat(findPost.get().getWriter()).isEqualTo(saved.getWriter());
        assertThat(findPost.get().getContent()).isEqualTo(saved.getContent());
        assertThat(findPost.get().getPassword()).isEqualTo(saved.getPassword());
        assertThat(findPost.get().getCreatedDate()).isEqualTo(saved.getCreatedDate());
    }

    @Test
    void 게시글_리스트를_조회할_수_있다_체크된_공지사항_순서() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post1 = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .writer("황진이")
                .content("내용2")
                .notice(true)
                .password("777")
                .createdDate(LocalDateTime.of(2023,10,15,13,13))
                .build();
        Post saved1 = testContainer.postService.savePost(post1);
        Post saved2 = testContainer.postService.savePost(post2);

        //when
        Page<Post> posts = testContainer.postService.findListOrderby(0, "notice");

        //then
        assertThat(posts.getNumberOfElements()).isEqualTo(2);
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("제목2");
        assertThat(posts.getContent().get(1).getTitle()).isEqualTo("제목1");
    }

    @Test
    void 게시글_한_건_조회시_항상_비밀글_여부를_체크하고_비밀번호를_입력받아야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(true)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post saved = testContainer.postService.savePost(post);

        //when
        Optional<Post> findPost = testContainer.postService.findSecretPostById(saved.getId(), "qwer1234");

        //then
        assertThat(findPost.isPresent()).isTrue();
        assertThat(saved.getTitle()).isEqualTo(findPost.get().getTitle());
        assertThatThrownBy(() -> testContainer.postService.findSecretPostById(saved.getId(), "wrong Password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 이전글_조회가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post1 = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .writer("황진이")
                .content("내용2")
                .notice(false)
                .secret(true)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .writer("척준경")
                .content("내용3")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post saved1 = testContainer.postService.savePost(post1);
        testContainer.postService.savePost(post2);
        Post saved3 = testContainer.postService.savePost(post3);

        //when
        Post prev = testContainer.postService.prevPost(saved3.getId());

        //then
        assertThat(prev.getTitle()).isEqualTo(saved1.getTitle());
        assertThat(prev.getContent()).isEqualTo(saved1.getContent());
        assertThat(prev.getId()).isEqualTo(saved1.getId());
    }

    @Test
    void 다음글_조회가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post1 = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .writer("황진이")
                .content("내용2")
                .notice(false)
                .secret(true)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .writer("척준경")
                .content("내용3")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post saved1 = testContainer.postService.savePost(post1);
        testContainer.postService.savePost(post2);
        Post saved3 = testContainer.postService.savePost(post3);

        //when
        Post next = testContainer.postService.nextPost(saved1.getId());

        //then
        assertThat(next.getTitle()).isEqualTo(saved3.getTitle());
        assertThat(next.getContent()).isEqualTo(saved3.getContent());
        assertThat(next.getId()).isEqualTo(saved3.getId());
    }

    @Test
    void 게시글_삭제가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Post post = Post.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023,10,15,13,12))
                .build();
        Post saved = testContainer.postRepository.save(post);

        //when
        testContainer.postService.remove(saved.getId(), "qwer1234");

        //then
        assertThatThrownBy(() -> testContainer.postRepository.findById(saved.getId()))
                .isInstanceOf(NoPostException.class);
    }
}

package com.hansol.board.post;

import com.hansol.board.common.domain.Writer;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.mock.TestContainer;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class PostServiceTest {
    @Test
    void 게시글_저장이_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .password("qwer1234")
                .build();

        //when
        PostEntity saved = testContainer.postService.savePost(post, new ArrayList<>());

        //then
        assertThat(saved.getTitle()).isEqualTo("제목1");
    }
    @Test
    void 게시글_한_건을_조회할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .password("qwer1234")
                .build();
        PostEntity saved = testContainer.postService.savePost(post, new ArrayList<>());

        //when
        PostEntity findPost = testContainer.postService.findPostById(saved.getId());

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(saved.getTitle());
        assertThat(findPost.getWriter()).isEqualTo(saved.getWriter());
        assertThat(findPost.getContent()).isEqualTo(saved.getContent());
        assertThat(findPost.getPassword()).isEqualTo(saved.getPassword());
        assertThat(findPost.getCreatedDate()).isEqualTo(saved.getCreatedDate());
    }

    @Test
    void 게시글_리스트를_조회할_수_있다_체크된_공지사항_순서() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post1 = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .password("qwer1234")
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("제목2")
                .writer("황진이")
                .content("내용2")
                .notice(true)
                .password("777")
                .build();
        PostEntity saved1 = testContainer.postService.savePost(post1, new ArrayList<>());
        PostEntity saved2 = testContainer.postService.savePost(post2, new ArrayList<>());

        //when
        Page<PostEntity> posts = testContainer.postService.findListOrderby(0, "notice");

        //then
        assertThat(posts.getNumberOfElements()).isEqualTo(2);
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo("제목2");
        assertThat(posts.getContent().get(1).getTitle()).isEqualTo("제목1");
    }

    @Test
    void 게시글_한_건_조회시_항상_비밀글_여부를_체크하고_비밀번호를_입력받아야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(true)
                .password("qwer1234")
                .build();
        PostEntity saved = testContainer.postService.savePost(post, new ArrayList<>());

        //when
        PostEntity findPost = testContainer.postService.findSecretPostById(saved.getId(), "qwer1234");

        //then
        assertThat(findPost).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(findPost.getTitle());
        assertThatThrownBy(() -> testContainer.postService.findSecretPostById(saved.getId(), "wrong Password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 이전글_조회가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post1 = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("제목2")
                .writer("황진이")
                .content("내용2")
                .notice(false)
                .secret(true)
                .password("qwer1234")
                .build();
        PostEntity post3 = PostEntity.builder()
                .title("제목3")
                .writer("척준경")
                .content("내용3")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .build();
        PostEntity saved1 = testContainer.postService.savePost(post1, new ArrayList<>());
        testContainer.postService.savePost(post2, new ArrayList<>());
        PostEntity saved3 = testContainer.postService.savePost(post3, new ArrayList<>());

        //when
        PostEntity prev = testContainer.postService.prevPost(saved3.getId());

        //then
        assertThat(prev.getTitle()).isEqualTo(saved1.getTitle());
        assertThat(prev.getContent()).isEqualTo(saved1.getContent());
        assertThat(prev.getId()).isEqualTo(saved1.getId());
    }

    @Test
    void 다음글_조회가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post1 = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("제목2")
                .writer("황진이")
                .content("내용2")
                .notice(false)
                .secret(true)
                .password("qwer1234")
                .build();
        PostEntity post3 = PostEntity.builder()
                .title("제목3")
                .writer("척준경")
                .content("내용3")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .build();
        PostEntity saved1 = testContainer.postService.savePost(post1, new ArrayList<>());
        testContainer.postService.savePost(post2, new ArrayList<>());
        PostEntity saved3 = testContainer.postService.savePost(post3, new ArrayList<>());

        //when
        PostEntity next = testContainer.postService.nextPost(saved1.getId());

        //then
        assertThat(next.getTitle()).isEqualTo(saved3.getTitle());
        assertThat(next.getContent()).isEqualTo(saved3.getContent());
        assertThat(next.getId()).isEqualTo(saved3.getId());
    }

    @Test
    void 게시글_삭제가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        PostEntity post = PostEntity.builder()
                .title("제목1")
                .writer("홍길동")
                .content("내용1")
                .notice(false)
                .secret(false)
                .password("qwer1234")
                .build();
        PostEntity saved = testContainer.postRepository.save(post);

        //when
        testContainer.postService.remove(saved.getId(), "qwer1234");

        //then
        assertThatThrownBy(() -> testContainer.postRepository.findById(saved.getId()))
                .isInstanceOf(NoPostException.class);
    }
}

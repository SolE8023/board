package com.hansol.board.post.domain;

import com.hansol.board.post.form.SavePostForm;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PostTest {
    @Test
    void PostEntity에서_POST로_변환할_수_있어야_한다() {
        //given
        PostEntity entity = PostEntity.builder()
                .id(1L)
                .title("제목123")
                .writer("홍길동")
                .content("내용123")
                .code("notice")
                .secret(true)
                .notice(true)
                .password("qwer1234")
                .build();

        //when
        Post post = Post.fromEntity(entity);

        //then
        assertThat(post.getId()).isEqualTo(entity.getId());
        assertThat(post.getTitle()).isEqualTo(entity.getTitle());
        assertThat(post.getWriter()).isEqualTo(entity.getWriter());
        assertThat(post.getContent()).isEqualTo(entity.getContent());
        assertThat(post.getCode()).isEqualTo(entity.getCode());
        assertThat(post.getSecret()).isEqualTo(entity.getSecret());
        assertThat(post.getNotice()).isEqualTo(entity.getNotice());
        assertThat(post.getPassword()).isEqualTo(entity.getPassword());
    }

    @Test
    void PostForm에서_Post로_변환할_수_있어야_한다() {
        //given
        SavePostForm form = SavePostForm.builder()
                .title("제목123")
                .writer("홍길동")
                .content("내용123")
                .code("notice")
                .secret(true)
                .notice(true)
                .password("qwer1234")
                .createdDate(LocalDateTime.of(2023, 10, 21, 15, 13, 11))
                .lastModifiedDate(LocalDateTime.of(2023, 10, 21, 15, 13, 15))
                .build();

        //when
        Post post = Post.fromSaveForm(form);

        //then
        assertThat(post.getTitle()).isEqualTo(form.getTitle());
        assertThat(post.getWriter()).isEqualTo(form.getWriter());
        assertThat(post.getContent()).isEqualTo(form.getContent());
        assertThat(post.getCode()).isEqualTo(form.getCode());
        assertThat(post.getSecret()).isEqualTo(form.getSecret());
        assertThat(post.getNotice()).isEqualTo(form.getNotice());
        assertThat(post.getPassword()).isEqualTo(form.getPassword());
        assertThat(post.getCreatedDate()).isEqualTo(form.getCreatedDate());
        assertThat(post.getLastModifiedDate()).isEqualTo(form.getLastModifiedDate());
    }
}
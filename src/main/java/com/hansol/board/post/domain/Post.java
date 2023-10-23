package com.hansol.board.post.domain;

import com.hansol.board.post.form.SavePostForm;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Post {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean secret;
    private Boolean notice;
    private String password;
    private String code;

    @Builder
    public Post(Long id, String title, String writer, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Boolean secret, Boolean notice, String password, String code) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.secret = secret;
        this.notice = notice;
        this.password = password;
        this.code = code;
    }

    public static Post fromEntity(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .writer(postEntity.getWriter())
                .content(postEntity.getContent())
                .createdDate(postEntity.getCreatedDate())
                .lastModifiedDate(postEntity.getLastModifiedDate())
                .secret(postEntity.getSecret())
                .notice(postEntity.getNotice())
                .password(postEntity.getPassword())
                .code(postEntity.getCode())
                .build();
    }

    public static Post formSaveForm(SavePostForm savePostForm) {
        return Post.builder()
                .title(savePostForm.getTitle())
                .writer(savePostForm.getWriter())
                .content(savePostForm.getContent())
                .createdDate(savePostForm.getCreatedDate())
                .lastModifiedDate(savePostForm.getLastModifiedDate())
                .secret(savePostForm.getSecret())
                .notice(savePostForm.getNotice())
                .password(savePostForm.getPassword())
                .code(savePostForm.getCode())
                .build();
    }
}

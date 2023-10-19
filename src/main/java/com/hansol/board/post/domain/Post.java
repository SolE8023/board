package com.hansol.board.post.domain;

import com.hansol.board.common.domain.Writer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Post {
    private Long id;
    private String title;
    private Writer writer;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean secret;
    private Boolean notice;
    private String password;
    private String code;

    @Builder
    public Post(Long id, String title, Writer writer, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Boolean secret, Boolean notice, String password, String code) {
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

    public static Post from(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .writer(Writer.builder().name(postEntity.getWriter()).build())
                .content(postEntity.getContent())
                .createdDate(postEntity.getCreatedDate())
                .lastModifiedDate(postEntity.getLastModifiedDate())
                .secret(postEntity.getSecret())
                .notice(postEntity.getNotice())
                .password(postEntity.getPassword())
                .code(postEntity.getCode())
                .build();
    }
}

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean secret;
    private Boolean notice;
    private String password;

    @Builder
    public Post(Long id, String title, Writer writer, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean secret, Boolean notice, String password) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.secret = secret;
        this.notice = notice;
        this.password = password;
    }
}

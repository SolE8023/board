package com.hansol.board.comment.domain;

import com.hansol.board.common.domain.Writer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comment {
    private Long id;
    private Long postId;
    private Writer writer;
    private LocalDateTime createdAt;
    private String content;
    private Boolean secret;
    private String password;

    @Builder
    public Comment(Long id, Long postId, Writer writer, LocalDateTime createdAt, String content, Boolean secret, String password) {
        this.id = id;
        this.postId = postId;
        this.writer = writer;
        this.createdAt = createdAt;
        this.content = content;
        this.secret = secret;
        this.password = password;
    }
}

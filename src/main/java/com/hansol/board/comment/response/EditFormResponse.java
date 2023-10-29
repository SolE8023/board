package com.hansol.board.comment.response;

import com.hansol.board.comment.domain.CommentEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class EditFormResponse {
    private Long id;
    private String writer;
    private String content;
    private Boolean secret;
    private String password;

    @Builder
    public EditFormResponse(Long id, String writer, String content, Boolean secret, String password) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.secret = secret;
        this.password = password;
    }

    public static EditFormResponse fromEntity(CommentEntity entity) {
        return EditFormResponse.builder()
                .id(entity.getId())
                .writer(entity.getWriter())
                .content(entity.getContent())
                .secret(entity.getSecret())
                .password(entity.getPassword())
                .build();
    }
}

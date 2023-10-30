package com.hansol.board.comment.response;

import com.hansol.board.comment.domain.CommentEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class ViewResponse {
    private Long id;
    private String content;

    @Builder
    public ViewResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static ViewResponse fromEntity(CommentEntity entity) {
        return ViewResponse.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .build();
    }
}

package com.hansol.board.comment.form;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveCommentForm {
    @NotEmpty
    private String writer;
    @NotEmpty
    private String content;
    private Boolean secret;
    @NotEmpty
    private String password;
    @NotNull
    private Long postId;
}

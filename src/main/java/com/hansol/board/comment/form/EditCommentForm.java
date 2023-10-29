package com.hansol.board.comment.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditCommentForm {
    @NotNull
    private Long id;
    @NotEmpty
    private String writer;
    @NotEmpty
    private String content;
    private Boolean secret;
    @NotEmpty
    @Size(min = 4, max = 8)
    private String password;
}

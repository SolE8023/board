package com.hansol.board.comment.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckPassword {
    @NotNull
    private Long id;
    @NotEmpty
    @Size(min = 4, max = 8)
    private String password;
    @NotEmpty
    private String type;
}

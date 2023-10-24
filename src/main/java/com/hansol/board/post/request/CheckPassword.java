package com.hansol.board.post.request;

import lombok.Data;

@Data
public class CheckPassword {
    private Long id;
    private String password;
    private String type;
}

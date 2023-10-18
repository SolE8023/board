package com.hansol.board.common.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Writer {
    private String name;
    @Builder
    public Writer(String name) {
        this.name = name;
    }
}

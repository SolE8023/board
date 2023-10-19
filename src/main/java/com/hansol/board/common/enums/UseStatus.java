package com.hansol.board.common.enums;

import lombok.Builder;
import lombok.Getter;

@Getter
public enum UseStatus {
    disable("사용 안 함"), enable("사용");
    private final String label;

    UseStatus(String label) {
        this.label = label;
    }
}

package com.hansol.board.exception;

public class NoBoardInfoException extends IllegalArgumentException {
    public NoBoardInfoException() {
        super("게시판 정보가 존재하지 않습니다.");
    }
}

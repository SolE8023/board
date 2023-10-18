package com.hansol.board.exception;

public class NoPostException extends IllegalArgumentException{
    public NoPostException() {
        super("게시글이 존재하지 않습니다.");
    }
}

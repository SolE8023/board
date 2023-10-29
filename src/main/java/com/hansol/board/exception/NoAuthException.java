package com.hansol.board.exception;

public class NoAuthException extends IllegalArgumentException{
    public NoAuthException() {
        super("권한이 없습니다.");
    }
}

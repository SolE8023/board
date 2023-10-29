package com.hansol.board.exception;

public class NoCommentException extends IllegalArgumentException{
    public NoCommentException() {
        super("댓글이 존재하지 않습니다.");
    }
}

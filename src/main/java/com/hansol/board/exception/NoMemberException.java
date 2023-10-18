package com.hansol.board.exception;

public class NoMemberException extends IllegalArgumentException{
    public NoMemberException() {
        super("회원이 존재하지 않습니다.");
    }
}

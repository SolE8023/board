package com.hansol.board.exception;

public class PasswordErrorException extends IllegalArgumentException{
    public PasswordErrorException() {
        super("비밀번호가 올바르지 않습니다.");
    }
}

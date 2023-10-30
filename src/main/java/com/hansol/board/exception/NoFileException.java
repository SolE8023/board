package com.hansol.board.exception;

public class NoFileException extends IllegalArgumentException{
    public NoFileException() {
        super("파일이 없습니다.");
    }
}

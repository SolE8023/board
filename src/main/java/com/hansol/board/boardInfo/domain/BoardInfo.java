package com.hansol.board.boardInfo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardInfo {
    private Long id;
    private String boardName;
    private String boardSkin;
    private Boolean comment;
    private String boardCode;
    private Integer fileUpload;

    @Builder
    public BoardInfo(Long id, String boardName, String boardSkin, Boolean comment, String boardCode, Integer fileUpload) {
        this.id = id;
        this.boardName = boardName;
        this.boardSkin = boardSkin;
        this.comment = comment;
        this.boardCode = boardCode;
        this.fileUpload = fileUpload;
    }
}

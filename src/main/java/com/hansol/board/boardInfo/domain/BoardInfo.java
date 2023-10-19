package com.hansol.board.boardInfo.domain;

import com.hansol.board.common.enums.UseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardInfo {
    private Long id;
    private String boardName;
    private String boardSkin;
    private UseStatus comment;
    private String boardCode;
    private Integer fileUpload;
    private UseStatus reply;

    @Builder
    public BoardInfo(Long id, String boardName, String boardSkin, UseStatus comment, String boardCode, Integer fileUpload, UseStatus reply) {
        this.id = id;
        this.boardName = boardName;
        this.boardSkin = boardSkin;
        this.comment = comment;
        this.boardCode = boardCode;
        this.fileUpload = fileUpload;
        this.reply = reply;
    }

    public static BoardInfo from(BoardInfoEntity boardInfoEntity) {
        return BoardInfo.builder()
                .id(boardInfoEntity.getId())
                .boardName(boardInfoEntity.getBoardName())
                .boardSkin(boardInfoEntity.getBoardSkin())
                .comment(boardInfoEntity.getComment())
                .boardCode(boardInfoEntity.getBoardCode())
                .fileUpload(boardInfoEntity.getFileUpload())
                .reply(boardInfoEntity.getReply())
                .build();
    }
}

package com.hansol.board.common.api.request;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.common.enums.UseStatus;
import lombok.Data;

@Data
public class BoardInfoUpdateRequest {
    private Long id;
    private String boardName;
    private String boardCode;
    private String boardSkin;
    private int fileUpload;
    private String reply;
    private String comment;

    public BoardInfo toModel() {
        return BoardInfo.builder()
                .id(id)
                .boardName(boardName)
                .boardCode(boardCode)
                .boardSkin(boardSkin)
                .fileUpload(fileUpload)
                .reply(UseStatus.valueOf(reply))
                .comment(UseStatus.valueOf(comment))
                .build();
    }
}

package com.hansol.board.common.api.request;

import lombok.Data;

@Data
public class BoardInfoSaveRequest {
    private String boardName;
    private String boardCode;
    private String boardSkin;
}

package com.hansol.board.boardInfo.domain;

import com.hansol.board.common.enums.UseStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "board_info")
public class BoardInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter private String boardName;
    @Setter private String boardSkin;
    @Setter @Enumerated(EnumType.STRING) private UseStatus comment;
    @Setter private String boardCode;
    @Setter private int fileUpload;
    @Setter @Enumerated(EnumType.STRING) private UseStatus reply;

    @Builder
    public BoardInfoEntity(Long id, String boardName, String boardSkin, UseStatus comment, String boardCode, Integer fileUpload, UseStatus reply) {
        this.id = id;
        this.boardName = boardName;
        this.boardSkin = boardSkin;
        this.comment = comment;
        this.boardCode = boardCode;
        this.fileUpload = fileUpload;
        this.reply = reply;
    }

    public static BoardInfoEntity from(BoardInfo boardInfo) {
        return BoardInfoEntity.builder()
                .boardName(boardInfo.getBoardName())
                .boardSkin(boardInfo.getBoardSkin())
                .comment(boardInfo.getComment())
                .boardCode(boardInfo.getBoardCode())
                .fileUpload(boardInfo.getFileUpload())
                .reply(boardInfo.getReply())
                .build();
    }
}

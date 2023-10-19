package com.hansol.board.boardInfo;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.exception.NoBoardInfoException;
import com.hansol.board.mock.TestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class BoardInfoRepositoryTest {
    @Test
    void 게시판_정보_추가가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        BoardInfo boardInfo = BoardInfo.builder()
                .boardName("공지사항")
                .boardSkin("notice")
                .comment(true)
                .boardCode("notice")
                .fileUpload(10)
                .build();

        //when
        BoardInfo saved = testContainer.boardInfoRepository.save(boardInfo);

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBoardName()).isEqualTo("공지사항");
    }

    @Test
    void 게시판_정보_삭제가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        BoardInfo boardInfo = BoardInfo.builder()
                .boardName("공지사항")
                .boardSkin("notice")
                .comment(true)
                .boardCode("notice")
                .fileUpload(10)
                .build();
        BoardInfo saved = testContainer.boardInfoRepository.save(boardInfo);

        //when
        testContainer.boardInfoRepository.remove(boardInfo.getId());

        //then
        assertThatThrownBy(() -> testContainer.boardInfoRepository.findById(saved.getId()))
                .isInstanceOf(NoBoardInfoException.class);
    }

    @Test
    void 게시판_정보_수정이_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        BoardInfo boardInfo = BoardInfo.builder()
                .boardName("공지사항")
                .boardSkin("notice")
                .comment(true)
                .boardCode("notice")
                .fileUpload(10)
                .build();
        BoardInfo saved = testContainer.boardInfoRepository.save(boardInfo);

        BoardInfo update = BoardInfo.builder()
                .id(saved.getId())
                .boardName("자유게시판")
                .boardSkin("free")
                .comment(true)
                .boardCode("free")
                .fileUpload(10)
                .build();

        //when
        BoardInfo updated = testContainer.boardInfoRepository.update(saved.getId(), update);

        //then
        assertThat(updated.getBoardName()).isEqualTo(update.getBoardName());
        assertThat(updated.getBoardSkin()).isEqualTo(update.getBoardSkin());
        assertThat(updated.getBoardCode()).isEqualTo(update.getBoardCode());

    }

    @Test
    void 게시판_정보_조회가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        BoardInfo boardInfo = BoardInfo.builder()
                .boardName("공지사항")
                .boardSkin("notice")
                .comment(true)
                .boardCode("notice")
                .fileUpload(10)
                .build();

        //when
        BoardInfo saved = testContainer.boardInfoRepository.save(boardInfo);

        //then
        BoardInfo findBoardInfo = testContainer.boardInfoRepository.findById(saved.getId());

        assertThat(findBoardInfo.getBoardName()).isEqualTo(saved.getBoardName());
        assertThat(findBoardInfo.getBoardSkin()).isEqualTo(saved.getBoardSkin());
        assertThat(findBoardInfo.getBoardCode()).isEqualTo(saved.getBoardCode());
    }

    @Test
    void 게시판의_모든_정보_조회가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        BoardInfo boardInfo1 = BoardInfo.builder()
                .boardName("공지사항")
                .boardSkin("notice")
                .comment(true)
                .boardCode("notice")
                .fileUpload(10)
                .build();
        BoardInfo boardInfo2 = BoardInfo.builder()
                .boardName("자유게시판")
                .boardSkin("free")
                .comment(true)
                .boardCode("free")
                .fileUpload(10)
                .build();

        //when
        BoardInfo saved1 = testContainer.boardInfoRepository.save(boardInfo1);
        BoardInfo saved2 = testContainer.boardInfoRepository.save(boardInfo2);

        //then
        List<BoardInfo> boardInfos = testContainer.boardInfoRepository.findAll();

        assertThat(boardInfos.size()).isEqualTo(2);
    }
}

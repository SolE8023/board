package com.hansol.board.mock;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.exception.NoBoardInfoException;

import java.util.ArrayList;
import java.util.List;

public class FakeBoardInfoRepository implements BoardInfoRepository {
    private Long id = 0L;
    private List<BoardInfo> boardInfos = new ArrayList<>();
    @Override
    public BoardInfo save(BoardInfo boardInfo) {
        if (boardInfo.getId() == null || boardInfo.getId() == 0L) {
            boardInfo.setId(++id);
            boardInfos.add(boardInfo);
        } else {
            throw new IllegalArgumentException("잘못된 boardInfoId 입니다.");
        }
        return boardInfo;
    }

    @Override
    public void remove(Long id) {
        boardInfos.removeIf(b -> b.getId().equals(id));
    }

    @Override
    public BoardInfo findById(Long id) {
        return boardInfos.stream()
                .filter(b -> b.getId().equals(id))
                .findAny()
                .orElseThrow(NoBoardInfoException::new);
    }

    @Override
    public BoardInfo update(Long id, BoardInfo boardInfo) {
        boardInfos.removeIf(b -> b.getId().equals(boardInfo.getId()));
        boardInfos.add(boardInfo);
        return boardInfo;
    }

    @Override
    public List<BoardInfo> findAll() {
        return boardInfos;
    }

    @Override
    public BoardInfo findByBoardCode(String code) {
        return null;
    }
}

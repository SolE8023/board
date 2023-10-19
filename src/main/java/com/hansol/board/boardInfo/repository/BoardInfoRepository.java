package com.hansol.board.boardInfo.repository;

import com.hansol.board.boardInfo.domain.BoardInfo;

import java.util.List;

public interface BoardInfoRepository {
    BoardInfo save(BoardInfo boardInfo);

    void remove(Long id);

    BoardInfo findById(Long id);

    BoardInfo update(Long id, BoardInfo boardInfo);

    List<BoardInfo> findAll();

    BoardInfo findByBoardCode(String code);
}

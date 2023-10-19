package com.hansol.board.boardInfo.repository;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.domain.BoardInfoEntity;
import com.hansol.board.exception.NoBoardInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardInfoRepositoryImpl implements BoardInfoRepository{
    private final BoardInfoJpaRepository jpaRepository;

    @Override
    public BoardInfo save(BoardInfo boardInfo) {
        BoardInfoEntity entity = jpaRepository.save(BoardInfoEntity.from(boardInfo));
        return BoardInfo.from(entity);
    }

    @Override
    public void remove(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public BoardInfo findById(Long id) {
        return jpaRepository.findById(id)
                .map(BoardInfo::from)
                .orElseThrow(NoBoardInfoException::new);
    }

    @Transactional
    @Override
    public BoardInfo update(Long id, BoardInfo boardInfo) {
        Optional<BoardInfoEntity> entity = jpaRepository.findById(id);
        entity.ifPresent(e -> {
            e.setBoardCode(boardInfo.getBoardCode());
            e.setBoardSkin(boardInfo.getBoardSkin());
            e.setBoardName(boardInfo.getBoardName());
            e.setComment(boardInfo.getComment());
            e.setFileUpload(boardInfo.getFileUpload());
            e.setReply(boardInfo.getReply());
        });
        entity.orElseThrow(NoBoardInfoException::new);
        return BoardInfo.from(entity.get());
    }

    @Override
    public List<BoardInfo> findAll() {
        return jpaRepository.findAll().stream().map(BoardInfo::from).toList();
    }

    @Override
    public BoardInfo findByBoardCode(String code) {
        Optional<BoardInfoEntity> findInfo = jpaRepository.findByBoardCode(code);
        return findInfo.map(BoardInfo::from).orElseThrow(NoBoardInfoException::new);
    }
}

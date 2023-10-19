package com.hansol.board.boardInfo.repository;

import com.hansol.board.boardInfo.domain.BoardInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardInfoJpaRepository extends JpaRepository<BoardInfoEntity, Long> {
    Optional<BoardInfoEntity> findByBoardCode(String code);
}

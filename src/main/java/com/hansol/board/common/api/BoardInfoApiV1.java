package com.hansol.board.common.api;

import com.hansol.board.common.api.request.BoardInfoSaveRequest;
import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.common.api.request.BoardInfoUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/board-info")
@RequiredArgsConstructor
public class BoardInfoApiV1 {
    private final BoardInfoRepository repository;
    @PostMapping
    public ResponseEntity<String> save(@RequestBody BoardInfoSaveRequest request) {
        if (!StringUtils.hasText(request.getBoardName())
        ||!StringUtils.hasText(request.getBoardSkin())
        ||!StringUtils.hasText(request.getBoardCode())) {
            return ResponseEntity.status(400).body("값이 누락되었습니다. boardName, boardSkin, boardCode 의 값을 모두 입력해 주세요.");
        }
        BoardInfo boardInfo = BoardInfo.builder()
                .boardName(request.getBoardName())
                .boardSkin(request.getBoardSkin())
                .boardCode(request.getBoardCode())
                .build();
        BoardInfo info = repository.save(boardInfo);
        if (info.getId() != null || !info.getId().equals(0L)) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(500).body("fail");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        BoardInfo findInfo = repository.findById(id);
        if (findInfo.getBoardCode().equals("notice")) {
            return ResponseEntity.status(400).body("공지사항은 삭제를 막아두었습니다.");
        } else {
            repository.remove(id);
        }

        return ResponseEntity.ok("success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody BoardInfoUpdateRequest request, @PathVariable Long id) {
        BoardInfo info = request.toModel();
        log.info("request={}", request);
        log.info("info={}", info.toString());
        repository.update(id, info);
        return ResponseEntity.ok("success");
    }
}

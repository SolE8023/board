package com.hansol.board.common.api;

import com.hansol.board.attachment.domain.Attachment;
import com.hansol.board.attachment.repository.AttachmentRepository;
import com.hansol.board.exception.NoAuthException;
import com.hansol.board.exception.NoFileException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static com.hansol.board.common.Constant.FILE_PATH;

@Slf4j
@RestController
@RequestMapping("/api/v1/attachment")
@RequiredArgsConstructor
public class AttachmentApiV1 {
    private final AttachmentRepository repository;
    @GetMapping("download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Optional<Attachment> findFile = repository.findById(id);
        Attachment file = findFile.orElseThrow(NoFileException::new);
        String filePath = FILE_PATH + file.getCode() + "/" + file.getSavedFileName();
        Resource resource = new FileSystemResource(filePath);

        // 파일의 MIME 타입 확인하여 ContentType 설정
        String contentType = null;

        try {
            contentType = Files.probeContentType(Paths.get(filePath));
        } catch (IOException e) {
            contentType = "application/octet-stream"; // 기본 바이너리 타입 설정
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("{fileId}")
    public ResponseEntity<String> delete(@PathVariable Long fileId, HttpSession session) {
        Boolean auth = (Boolean) session.getAttribute("auth");
        String type = (String) session.getAttribute("type");
        Long id = (Long) session.getAttribute("id");

        Optional<Attachment> findFile = repository.findById(fileId);
        Attachment find = findFile.orElseThrow(NoFileException::new);

        if (auth && type.equals("edit") && id.equals(find.getPost().getId())) {
            String filePath = FILE_PATH + find.getCode() + "/" + find.getSavedFileName();
            File file = new File(filePath);
            if (file.delete()) {
                repository.deleteById(fileId);
                return ResponseEntity.ok().body("success");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 삭제 실패");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("권한 없음");
        }
    }
}

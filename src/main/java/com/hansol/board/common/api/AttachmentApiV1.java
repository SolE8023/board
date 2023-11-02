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
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static com.hansol.board.common.Constant.FILE_PATH;

@Slf4j
@RestController
@RequestMapping("/api/v1/attachment")
@RequiredArgsConstructor
public class AttachmentApiV1 {
    private final AttachmentRepository repository;
    @GetMapping("download/{id}")
    public Object download(@PathVariable Long id) {
        Optional<Attachment> findFile = repository.findById(id);
        if(findFile.isEmpty()) return ResponseEntity.badRequest().body("파일이 없습니다");
        Attachment file = findFile.get();


        String savedFileName = file.getSavedFileName();
        String originFileName = file.getOriginFileName();

        FileSystemResource resource;

        try {
            resource = new FileSystemResource(FILE_PATH + file.getCode() + "/" + savedFileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("파일이 없습니다");
        }


        String encodedOriginalFileName = UriUtils.encode(originFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
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
            String thumbnailPath = null;

            if(StringUtils.hasText(find.getThumbnail()))
                thumbnailPath = FILE_PATH + find.getCode() + "/" + find.getThumbnail();

            File file = new File(filePath);
            if (file.delete()) {
                if (thumbnailPath != null) {
                    File thumbnail = new File(thumbnailPath);
                    thumbnail.delete();
                }
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

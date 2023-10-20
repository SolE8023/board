package com.hansol.board.post.form;

import com.hansol.board.common.domain.Writer;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostForm {
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean secret;
    private Boolean notice;
    private String password;
    private String code;
    private int fileUpload;
    private List<MultipartFile> files;
}

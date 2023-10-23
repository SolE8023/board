package com.hansol.board.post.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EditPostForm {
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

    @Builder
    public EditPostForm(String title, String writer, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Boolean secret, Boolean notice, String password, String code, int fileUpload, List<MultipartFile> files) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.secret = secret;
        this.notice = notice;
        this.password = password;
        this.code = code;
        this.fileUpload = fileUpload;
        this.files = files;
    }
}

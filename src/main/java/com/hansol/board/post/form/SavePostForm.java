package com.hansol.board.post.form;

import com.hansol.board.post.domain.Post;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SavePostForm {
    @NotEmpty
    private String title;
    @NotEmpty
    private String writer;
    @NotEmpty
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean secret;
    private Boolean notice;
    @NotEmpty
    @Size(min = 4, max = 8)
    private String password;
    @NotEmpty
    private String code;
    private Integer fileUpload;
    private List<MultipartFile> files = new ArrayList<>();
    private Long parentId;

    @Builder
    public SavePostForm(String title, String writer, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Boolean secret, Boolean notice, String password, String code, Integer fileUpload, List<MultipartFile> files, Long parentId) {
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
        this.parentId = parentId;
    }
}

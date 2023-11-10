package com.hansol.board.post.form;

import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EditPostForm {
    @NotNull
    private Long id;
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

    @Builder
    public EditPostForm(Long id, String title, String writer, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Boolean secret, Boolean notice, String password, String code, Integer fileUpload, List<MultipartFile> files) {
        this.id = id;
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

    public static EditPostForm from(Post post) {
        return EditPostForm.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writer(post.getWriter())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .secret(post.getSecret())
                .notice(post.getNotice())
                .password(post.getPassword())
                .code(post.getCode())
                .build();
    }

    public static EditPostForm fromEntity(PostEntity entity) {
        return EditPostForm.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .content(entity.getContent())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .secret(entity.getSecret())
                .notice(entity.getNotice())
                .password(entity.getPassword())
                .code(entity.getCode())
                .build();
    }
}

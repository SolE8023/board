package com.hansol.board.post.domain;

import com.hansol.board.common.domain.BaseEntity;
import com.hansol.board.common.domain.Writer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Setter private String title;
    @Setter private String writer;
    @Setter private String content;
    @Setter private Boolean secret;
    @Setter private Boolean notice;
    @Setter private String password;
    @Setter private String code;

    @Builder
    public PostEntity(Long id, String title, String writer, String content, Boolean secret, Boolean notice, String password, String code) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.secret = secret;
        this.notice = notice;
        this.password = password;
        this.code = code;
    }

    public static PostEntity from(Post post) {
        return PostEntity.builder()
                .title(post.getTitle())
                .writer(post.getWriter().getName())
                .content(post.getContent())
                .secret(post.getSecret())
                .notice(post.getNotice())
                .password(post.getPassword())
                .code(post.getCode())
                .build();
    }
}

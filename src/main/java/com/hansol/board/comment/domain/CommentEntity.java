package com.hansol.board.comment.domain;

import com.hansol.board.comment.form.EditCommentForm;
import com.hansol.board.comment.form.SaveCommentForm;
import com.hansol.board.common.domain.BaseEntity;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Setter private Long id;
    @Setter private String writer;
    @Setter private String content;
    @Setter private Boolean secret;
    @Setter private String password;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Builder
    public CommentEntity(Long id, String writer, String content, Boolean secret, String password, PostEntity post) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.secret = secret;
        this.password = password;
        this.post = post;
    }

    public static CommentEntity fromSaveForm(SaveCommentForm form, PostEntity post) {
        return CommentEntity.builder()
                .writer(form.getWriter())
                .content(form.getContent())
                .secret(form.getSecret())
                .password(form.getPassword())
                .post(post)
                .build();
    }

    public static CommentEntity fromEditForm(EditCommentForm form) {
        return CommentEntity.builder()
                .id(form.getId())
                .writer(form.getWriter())
                .password(form.getPassword())
                .content(form.getContent())
                .secret(form.getSecret())
                .build();
    }
}

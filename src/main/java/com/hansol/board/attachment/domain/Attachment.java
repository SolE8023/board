package com.hansol.board.attachment.domain;

import com.hansol.board.common.domain.BaseEntity;
import com.hansol.board.post.domain.PostEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Attachment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;
    private String originFileName;
    private String savedFileName;
    private String code;

    @Builder
    public Attachment(Long id, PostEntity post, String originFileName, String savedFileName, String code) {
        this.id = id;
        this.post = post;
        this.originFileName = originFileName;
        this.savedFileName = savedFileName;
        this.code = code;
    }
}

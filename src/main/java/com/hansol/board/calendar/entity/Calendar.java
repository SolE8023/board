package com.hansol.board.calendar.entity;

import com.hansol.board.attachment.domain.Attachment;
import com.hansol.board.calendar.form.EditForm;
import com.hansol.board.calendar.form.SaveForm;
import com.hansol.board.comment.domain.CommentEntity;
import com.hansol.board.common.domain.BaseEntity;
import com.hansol.board.post.domain.PostEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Calendar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;
    @Setter private String title;
    @Setter private String writer;

    @Column(columnDefinition = "text")
    @Setter private String content;

    @Setter private String password;
    @Setter private String code;

    @Setter private LocalDate startDate;
    @Setter private LocalDate endDate;
    @Setter private String time;
    @Setter private String place;

    @Builder
    public Calendar(Long id, String title, String writer, String content, String password, String code, LocalDate startDate, LocalDate endDate, String time, String place) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.password = password;
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.place = place;
    }

    public static Calendar fromSaveForm(SaveForm form) {
        return Calendar.builder()
                .title(form.getTitle())
                .writer(form.getWriter())
                .content(form.getContent())
                .password(form.getPassword())
                .code(form.getCode())
                .startDate(form.getStartDate())
                .endDate(form.getEndDate())
                .time(form.getTime())
                .place(form.getPlace())
                .build();
    }

    public static Calendar fromEditForm(EditForm form) {
        return Calendar.builder()
                .id(form.getId())
                .title(form.getTitle())
                .writer(form.getWriter())
                .content(form.getContent())
                .password(form.getPassword())
                .code(form.getCode())
                .startDate(form.getStartDate())
                .endDate(form.getEndDate())
                .time(form.getTime())
                .place(form.getPlace())
                .build();
    }
}

package com.hansol.board.calendar.form;

import com.hansol.board.calendar.entity.Calendar;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EditForm {
    @NotNull
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String writer;
    private String content;
    @NotEmpty
    @Size(min = 4, max = 8)
    private String password;
    @NotEmpty
    private String code;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String time;
    private String place;

    @Builder
    public EditForm(Long id, String title, String writer, String content, String password, String code, LocalDate startDate, LocalDate endDate, String time, String place) {
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

    public static EditForm fromEntity(Calendar entity) {
        return EditForm.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .content(entity.getContent())
                .password(entity.getPassword())
                .code(entity.getCode())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .time(entity.getTime())
                .place(entity.getPlace())
                .build();
    }
}

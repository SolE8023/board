package com.hansol.board.calendar.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class SaveForm {
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
}

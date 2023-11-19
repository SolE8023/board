package com.hansol.board.calendar.service;

import com.hansol.board.calendar.entity.Calendar;
import com.hansol.board.calendar.form.EditForm;
import com.hansol.board.calendar.repository.CalendarRepository;
import com.hansol.board.calendar.request.CheckPassword;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository repository;

    public List<Calendar> getLongTermEvents(int year, int month) {
        Map<String, LocalDate> range = getMinMaxRange(year, month);
        return repository.getLongTermEvents(range.get("startDate"), range.get("endDate"));
    }

    public List<Calendar> getSingleDayEvents(int year, int month) {
        Map<String, LocalDate> range = getMinMaxRange(year, month);
        return repository.getSingleDayEvents(range.get("startDate"), range.get("endDate"));
    }

    private Map<String, LocalDate> getMinMaxRange(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDayOfMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, lastDayOfMonth);

        Map<String, LocalDate> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return result;
    }

    public Calendar findCalendar(Long id) {
        Optional<Calendar> find = repository.findById(id);
        return find.orElse(null);
    }

    public Boolean checkPassword(CheckPassword request) {
        Optional<Calendar> find = repository.findByIdAndPassword(request.getId(), request.getPassword());
        return find.isPresent();
    }

    @Transactional
    public void update(EditForm form) {
        Optional<Calendar> find = repository.findById(form.getId());
        Calendar calendar = find.orElseThrow(IllegalArgumentException::new);

        calendar.setTitle(form.getTitle());
        calendar.setWriter(form.getWriter());
        calendar.setContent(form.getContent());
        calendar.setPassword(form.getPassword());
        calendar.setCode(form.getCode());
        calendar.setStartDate(form.getStartDate());
        calendar.setEndDate(form.getEndDate());
        calendar.setTime(form.getTime());
        calendar.setPlace(form.getPlace());
    }
}

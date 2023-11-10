package com.hansol.board.calendar.controller;

import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.calendar.entity.Calendar;
import com.hansol.board.calendar.form.SaveForm;
import com.hansol.board.calendar.repository.CalendarRepository;
import com.hansol.board.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final String CODE = "calendar";
    private final BoardInfoRepository boardInfoRepository;
    private final CalendarService calendarService;
    private final CalendarRepository repository;

    @GetMapping("list")
    public String list(@RequestParam(required = false) Integer year,
                       @RequestParam(required = false) Integer month,
                       Model model) {

        int selectYear = (year != null) ? year : LocalDate.now().getYear();
        int selectMonth = (month != null) ? month : LocalDate.now().getMonthValue();

        List<Calendar> longTerm = calendarService.getLongTermEvents(selectYear, selectMonth);
        List<Calendar> singleDay = calendarService.getSingleDayEvents(selectYear, selectMonth);
        log.info("longTerm 갯수: {}", longTerm.size());
        log.info("singleDay 갯수: {}", singleDay.size());
        model.addAttribute("longTerm", longTerm);
        model.addAttribute("singleDay", singleDay);
        model.addAttribute("boards", boardInfoRepository.findAll());
        model.addAttribute("boardName", "일정");

        return "board-skin/" + CODE + "/list";
    }

    @GetMapping("write")
    public String writeForm(Model model) {
        SaveForm form = new SaveForm();
        form.setCode(CODE);
        model.addAttribute("form", form);
        model.addAttribute("boards", boardInfoRepository.findAll());
        return "board-skin/" + CODE + "/write";
    }

    @PostMapping("write")
    public String addCalendar(@Validated @ModelAttribute("form") SaveForm saveForm,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boards", boardInfoRepository.findAll());
            return "board-skin/" + CODE + "/write";
        }
        Calendar calendar = Calendar.fromSaveForm(saveForm);
        Calendar saved = repository.save(calendar);

        redirectAttributes.addAttribute("code", CODE);
        redirectAttributes.addAttribute("id", saved.getId());
        return "redirect:/calendar/view/{id}";
    }

    @GetMapping("view/{id}")
    public String view(@PathVariable Long id,
                       Model model) {

        Calendar calendar = calendarService.findCalendar(id);

        model.addAttribute("calendar", calendar);
        model.addAttribute("boards", boardInfoRepository.findAll());

        return "board-skin/" + CODE + "/view";
    }
}

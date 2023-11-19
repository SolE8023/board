package com.hansol.board.calendar.controller;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.calendar.entity.Calendar;
import com.hansol.board.calendar.form.EditForm;
import com.hansol.board.calendar.form.SaveForm;
import com.hansol.board.calendar.repository.CalendarRepository;
import com.hansol.board.calendar.request.CheckPassword;
import com.hansol.board.calendar.service.CalendarService;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.form.EditPostForm;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @ResponseBody
    @PostMapping("chkPwd")
    public ResponseEntity<String> checkPassword(@RequestBody CheckPassword request,
                                                HttpSession session) {
        Boolean result = calendarService.checkPassword(request);
        if (result) {
            session.setAttribute("calendarAuth", true);
            session.setAttribute("type", request.getType());
            session.setAttribute("id", request.getId());
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 비밀번호입니다.");
        }
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {

        Boolean auth = (Boolean) session.getAttribute("calendarAuth");
        Long sessionPostId = (Long) session.getAttribute("id");
        String sessionType = (String) session.getAttribute("type");

        if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("delete")) {
            repository.deleteById(id);
            return "redirect:/calendar/list";
        } else {
            redirectAttributes.addAttribute("id", id);
            return "redirect:/calendar/view/{id}";
        }
    }

    @GetMapping("edit/{id}")
    public String editForm(@PathVariable Long id,
                           RedirectAttributes redirectAttributes,
                           Model model,
                           HttpSession session) {
        Optional<Calendar> find = repository.findById(id);
        Calendar calendar = find.orElse(null);
        if (calendar == null) {
            redirectAttributes.addAttribute("id", id);
            return "redirect:/calendar/view/{id}";
        }

        EditForm form = EditForm.fromEntity(calendar);
        form.setCode(CODE);

        model.addAttribute("form", form);
        model.addAttribute("boards", boardInfoRepository.findAll());

        Boolean auth = (Boolean) session.getAttribute("calendarAuth");
        Long sessionPostId = (Long) session.getAttribute("id");
        String sessionType = (String) session.getAttribute("type");

        if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("edit")) {
            return "board-skin/" + CODE + "/edit";
        } else {
            redirectAttributes.addAttribute("id", id);
            return "redirect:/calendar/view/{id}";
        }
    }

    @PostMapping("edit/{id}")
    public String edit(@Validated @ModelAttribute("form") EditForm editForm,
                       BindingResult bindingResult,
                       @PathVariable Long id,
                       Model model,
                       RedirectAttributes redirectAttributes,
                       HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boards", boardInfoRepository.findAll());
            return "board-skin/" + CODE + "/edit";
        }

        Boolean auth = (Boolean) session.getAttribute("calendarAuth");
        Long sessionPostId = (Long) session.getAttribute("id");
        String sessionType = (String) session.getAttribute("type");

        if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("edit")) {
            calendarService.update(editForm);
        }

        redirectAttributes.addAttribute("id", id);
        return "redirect:/calendar/view/{id}";
    }
}

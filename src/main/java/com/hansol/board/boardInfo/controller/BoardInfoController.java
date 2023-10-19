package com.hansol.board.boardInfo.controller;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.common.enums.UseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BoardInfoController {
    private final BoardInfoRepository repository;
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("infos", repository.findAll());
        model.addAttribute("skins", getSkins());
        return "/admin/manage-board";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        BoardInfo info = repository.findById(id);
        model.addAttribute("info", info);
        model.addAttribute("skins", getSkins());
        model.addAttribute("useStatus", UseStatus.values());
        return "/admin/edit-board-info";
    }

    private List<String> getSkins() {
        List<String> folderList = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource("templates/board-skin");
            File skinFolder = resource.getFile();
            File[] folders = skinFolder.listFiles(File::isDirectory);
            for (File folder : Objects.requireNonNull(folders)) {
                folderList.add(folder.getName());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return folderList;
    }
}

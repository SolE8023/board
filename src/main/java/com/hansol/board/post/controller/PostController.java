package com.hansol.board.post.controller;

import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.hansol.board.common.PageSetting.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final BoardInfoRepository boardInfoRepository;
    @GetMapping("{code}/list")
    public String list(Model model,
                       @PathVariable String code,
                       @RequestParam(required = false, defaultValue = "0") int page) {
        Page<Post> posts = postService.findAll(page, code);
        model.addAttribute("posts", posts);
        model.addAttribute("boards", boardInfoRepository.findAll());
        model.addAttribute("boardName", boardInfoRepository.findByBoardCode(code).getBoardName());

        setPageVariable(model, posts);

        return "/board-skin/" + code + "/list";
    }


}

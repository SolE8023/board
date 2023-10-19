package com.hansol.board.post.controller;

import com.hansol.board.post.domain.Post;
import com.hansol.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private PostService postService;
    @GetMapping
    public String list(Model model,
                       @PathVariable(required = false) String code,
                       @RequestParam int page) {
        code = setCode(code);
        Page<Post> posts = postService.findAll(page, code);
        model.addAttribute("posts", posts);
        return "/board-skin/" + code + "/list";
    }

    private String setCode(String code) {
        if(!StringUtils.hasText(code)) return "notice";
        else return code;
    }
}

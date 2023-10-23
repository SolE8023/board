package com.hansol.board.post.controller;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.form.SavePostForm;
import com.hansol.board.post.response.EditorResponse;
import com.hansol.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.hansol.board.common.Constant.FILE_PATH;
import static com.hansol.board.common.Constant.UPLOAD_FOLDER;
import static com.hansol.board.common.PageSetting.*;
import static com.hansol.board.common.Thumbnail.getExtension;

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
        Page<Post> posts = postService.findListOrderby(page, code);
        model.addAttribute("posts", posts);
        model.addAttribute("boards", boardInfoRepository.findAll());
        model.addAttribute("boardName", boardInfoRepository.findByBoardCode(code).getBoardName());

        setPageVariable(model, posts);

        return "/board-skin/" + code + "/list";
    }

    @GetMapping("{code}/write")
    public String writeForm(@PathVariable String code, Model model) {
        BoardInfo findInfo = boardInfoRepository.findByBoardCode(code);
        SavePostForm form = new SavePostForm();
        form.setFileUpload(findInfo.getFileUpload());
        form.setCode(code);
        model.addAttribute("form", form);
        model.addAttribute("boards", boardInfoRepository.findAll());
        return "/board-skin/" + code + "/write";
    }

    @PostMapping("{code}/write")
    public String addPost(@Validated @ModelAttribute("form") SavePostForm savePostForm,
                          BindingResult bindingResult,
                          @PathVariable String code,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boards", boardInfoRepository.findAll());
            return "/board-skin/" + code + "/write";
        }
        Post post = Post.formSaveForm(savePostForm);
        post = postService.savePost(post);

        redirectAttributes.addAttribute("code", code);
        redirectAttributes.addAttribute("id", post.getId());
        return "redirect:/post/{code}/view/{id}";
    }

    @GetMapping("{code}/view/{id}")
    public String view(@PathVariable String code,
                       @PathVariable Long id,
                       Model model) {
        Optional<Post> post = postService.findPostById(id);
        Post findPost = post.orElseThrow(NoPostException::new);
        model.addAttribute("post", findPost);
        model.addAttribute("boards", boardInfoRepository.findAll());
        return "/board-skin/" + code + "/view";
    }

    @ResponseBody
    @PostMapping("/file/upload")
    public EditorResponse uploadFile(@RequestParam(name = "upload") MultipartFile file,
                                     @RequestParam String code) throws IOException {
        if (!file.isEmpty()) {
            String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
            String savePath = FILE_PATH + "/" + code;

            File folder = new File(savePath);
            if (!folder.exists()) {
                boolean mkdirs = folder.mkdirs();
                if (!mkdirs) {
                    log.error("Failed to create folder: " + savePath);
                }
            }
            String filename = UUID.randomUUID() + "." + extension;
            String filePath = savePath + "/" + filename;
            file.transferTo(new File(filePath));

            String currentDomain = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String fileUrl = currentDomain + "/" + UPLOAD_FOLDER + "/" + code + "/" + filename;

            return EditorResponse.builder()
                    .filename(filename)
                    .uploaded(1)
                    .url(fileUrl)
                    .build();

        }
        return null;
    }
}

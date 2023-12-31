package com.hansol.board.post.controller;

import com.hansol.board.boardInfo.domain.BoardInfo;
import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.form.EditPostForm;
import com.hansol.board.post.form.SavePostForm;
import com.hansol.board.post.request.CheckPassword;
import com.hansol.board.post.response.EditorResponse;
import com.hansol.board.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Objects;
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
        Page<PostEntity> posts = postService.findListOrderby(page, code);
        model.addAttribute("posts", posts);
        model.addAttribute("boards", boardInfoRepository.findAll());
        model.addAttribute("boardName", boardInfoRepository.findByBoardCode(code).getBoardName());
        model.addAttribute("boardInfo", boardInfoRepository.findByBoardCode(code));

        setPageVariable(model, posts);

        String currentDomain = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String fileUrl = currentDomain + "/" + UPLOAD_FOLDER + "/" + code;
        model.addAttribute("fileUrl", fileUrl);

        return "board-skin/" + code + "/list";
    }

    @GetMapping("{code}/write")
    public String writeForm(@PathVariable String code, Model model) {
        BoardInfo findInfo = boardInfoRepository.findByBoardCode(code);
        SavePostForm form = new SavePostForm();
        form.setFileUpload(findInfo.getFileUpload());
        form.setCode(code);
        model.addAttribute("form", form);
        model.addAttribute("boards", boardInfoRepository.findAll());
        return "board-skin/" + code + "/write";
    }

    @GetMapping("{code}/reply/{id}")
    public String replyForm(@PathVariable String code,
                            @PathVariable Long id,
                            Model model) {
        SavePostForm form = new SavePostForm();
        form.setParentId(id);
        form.setCode(code);

        BoardInfo findInfo = boardInfoRepository.findByBoardCode(code);
        form.setFileUpload(findInfo.getFileUpload());

        model.addAttribute("form", form);
        model.addAttribute("boards", boardInfoRepository.findAll());
        return "board-skin/" + code + "/write";
    }

    @PostMapping("{code}/write")
    public String addPost(@Validated @ModelAttribute("form") SavePostForm savePostForm,
                          BindingResult bindingResult,
                          @PathVariable String code,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boards", boardInfoRepository.findAll());
            return "board-skin/" + code + "/write";
        }
        PostEntity entity = PostEntity.fromSaveForm(savePostForm);
        if (savePostForm.getParentId() != null && savePostForm.getParentId() != 0L) {
            PostEntity parentPost = postService.findPostById(savePostForm.getParentId());
            entity.setParentPost(parentPost);
        }

        PostEntity saved = postService.savePost(entity, savePostForm.getFiles());

        redirectAttributes.addAttribute("code", code);
        redirectAttributes.addAttribute("id", saved.getId());
        return "redirect:/post/{code}/view/{id}";
    }

    @GetMapping("{code}/view/{id}")
    public String view(@PathVariable String code,
                       @PathVariable Long id,
                       Model model,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        PostEntity post = postService.findPostById(id);

        model.addAttribute("post", post);
        model.addAttribute("files", post.getAttachments());
        model.addAttribute("boards", boardInfoRepository.findAll());
        model.addAttribute("boardInfo", boardInfoRepository.findByBoardCode(code));

        String currentDomain = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String fileUrl = currentDomain + "/" + UPLOAD_FOLDER + "/" + code;
        model.addAttribute("fileUrl", fileUrl);

        if (post.getSecret()) {
            Boolean auth = (Boolean) session.getAttribute("auth");
            Long sessionPostId = (Long) session.getAttribute("id");
            String sessionType = (String) session.getAttribute("type");
            if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("view")) {
                return "board-skin/" + code + "/view";
            } else {
                redirectAttributes.addAttribute("code", code);
                return "redirect:/post/{code}/list";
            }
        } else {
            return "board-skin/" + code + "/view";
        }
    }

    @GetMapping("{code}/edit/{id}")
    public String editForm(@PathVariable String code,
                           @PathVariable Long id,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        PostEntity entity = postService.findPostById(id);
        EditPostForm form = EditPostForm.fromEntity(entity);

        BoardInfo findInfo = boardInfoRepository.findByBoardCode(code);
        form.setFileUpload(findInfo.getFileUpload());
        form.setCode(code);

        model.addAttribute("form", form);
        model.addAttribute("files", entity.getAttachments());
        model.addAttribute("boards", boardInfoRepository.findAll());

        Boolean auth = (Boolean) session.getAttribute("auth");
        Long sessionPostId = (Long) session.getAttribute("id");
        String sessionType = (String) session.getAttribute("type");

        if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("edit")) {
            return "board-skin/" + code + "/edit";
        } else {
            redirectAttributes.addAttribute("code", code);
            redirectAttributes.addAttribute("id", id);
            return "redirect:/post/{code}/view/{id}";
        }
    }

    @PostMapping("{code}/edit/{id}")
    public String modifyPost(@Validated @ModelAttribute("form") EditPostForm editPostForm,
                             BindingResult bindingResult,
                             @PathVariable String code,
                             @PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boards", boardInfoRepository.findAll());
            return "board-skin/" + code + "/edit";
        }

        Boolean auth = (Boolean) session.getAttribute("auth");
        Long sessionPostId = (Long) session.getAttribute("id");
        String sessionType = (String) session.getAttribute("type");

        if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("edit")) {
            postService.update(PostEntity.formEditForm(editPostForm), editPostForm.getFiles());
        }

        redirectAttributes.addAttribute("code", code);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/post/{code}/view/{id}";
    }

    @PostMapping("{code}/delete/{id}")
    public String deletePost(@PathVariable String code,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        Boolean auth = (Boolean) session.getAttribute("auth");
        Long sessionPostId = (Long) session.getAttribute("id");
        String sessionType = (String) session.getAttribute("type");

        if (auth != null && auth && sessionPostId.equals(id) && sessionType.equals("delete")) {
            PostEntity entity = postService.findPostById(id);
            postService.remove(id, entity.getPassword());

            redirectAttributes.addAttribute("code", code);
            return "redirect:/post/{code}/list";
        } else {
            redirectAttributes.addAttribute("code", code);
            redirectAttributes.addAttribute("id", id);
            return "redirect:/post/{code}/view/{id}";
        }
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

    @ResponseBody
    @PostMapping("chkPwd")
    public ResponseEntity<String> checkPassword(@RequestBody CheckPassword request,
                                                HttpSession session) {
        Boolean result = postService.checkPassword(request);
        if (result) {
            session.setAttribute("auth", true);
            session.setAttribute("type", request.getType());
            session.setAttribute("id", request.getId());
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 비밀번호입니다.");
        }
    }

}

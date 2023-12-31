package com.hansol.board.post.service;

import com.hansol.board.attachment.domain.Attachment;
import com.hansol.board.attachment.repository.AttachmentRepository;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.repository.PostRepository;
import com.hansol.board.post.request.CheckPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.hansol.board.common.Constant.FILE_PATH;
import static com.hansol.board.common.Constant.UPLOAD_FOLDER;
import static com.hansol.board.common.Thumbnail.getExtension;
import static com.hansol.board.common.Thumbnail.makeThumbnail;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public PostEntity findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Page<PostEntity> findListOrderby(int page, String code) {
        return postRepository.findListOrderby(page, code);
    }

    @Override
    @Transactional
    public PostEntity savePost(PostEntity entity, List<MultipartFile> files) {
        PostEntity post = postRepository.save(entity);
        fileUpload(post, files);
        return post;
    }

    @Override
    @Transactional
    public PostEntity update(PostEntity post, List<MultipartFile> files) {
        PostEntity updated = postRepository.update(post);
        fileUpload(updated, files);
        return updated;
    }

    private void fileUpload(PostEntity post, List<MultipartFile> files) {
        String code = post.getCode();
        if(files == null) return;
        for (MultipartFile file : files) {
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
                try {
                    file.transferTo(new File(filePath));
                    String thumbnail = makeThumbnail(file, 300, 0, post.getCode(), filename);
                    Attachment attachment = Attachment.builder()
                            .post(post)
                            .originFileName(file.getOriginalFilename())
                            .savedFileName(filename)
                            .code(code)
                            .thumbnail(thumbnail)
                            .build();
                    attachmentRepository.save(attachment);
                } catch (IOException e) {
                    log.error("파일 업로드 실패");
                }
            }
        }
    }

    @Override
    public PostEntity findSecretPostById(Long id, String password) {
        if (postRepository.isSecretPost(id)) {
            if (postRepository.passwordCheck(id, password)) {
                return postRepository.findById(id);
            } else {
                throw new PasswordErrorException();
            }
        } else {
            return postRepository.findById(id);
        }
    }

    @Override
    public PostEntity prevPost(Long id) {
        return postRepository.findPrevPost(id);
    }

    @Override
    public PostEntity nextPost(Long id) {
        return postRepository.findNextPost(id);
    }

    @Override
    public void remove(Long id, String password) {
        PostEntity findPost = postRepository.findById(id);
        List<Attachment> files = findPost.getAttachments();
        if(files != null){
            for (Attachment file : files) {
                String filePath = FILE_PATH + findPost.getCode() + "/" + file.getSavedFileName();
                File saved = new File(filePath);
                saved.delete();
                String thumbnailPath = null;
                if (StringUtils.hasText(file.getThumbnail())) {
                    thumbnailPath = FILE_PATH + findPost.getCode() + "/" + file.getThumbnail();
                    File thumbnail = new File(thumbnailPath);
                    thumbnail.delete();
                }
            }
        }
        postRepository.remove(id, password);
    }

    @Override
    public Boolean checkPassword(CheckPassword request) {
        Optional<Post> findPost = postRepository.checkPassword(request.getId(), request.getPassword());
        return findPost.isPresent();
    }
}

package com.hansol.board.post.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EditorRequest {
    MultipartFile file;
}

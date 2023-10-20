package com.hansol.board.common;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class Thumbnail {
    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public String makeThumbnail(MultipartFile file, MultipartHttpServletRequest request, int thumbnailWidth, int thumbnailHeight, String saveFolder, String savedFileName) {
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        if (!isImageFile(file.getContentType())) return null;

        String boardCode = request.getParameter("boardCode");
        String imagePath = request.getSession().getServletContext().getRealPath("/" + saveFolder + "/" + boardCode);
        String thumbnailPath = request.getSession().getServletContext().getRealPath("/" + saveFolder + "/" + boardCode);

        File folder = new File(thumbnailPath);
        if (!folder.exists()) {
            boolean mkdirs = folder.mkdirs();
            if (!mkdirs) {
                log.error("Failed to create folder: " + thumbnailPath);
            }
        }

        imagePath = imagePath + "/" + savedFileName;
        String thumbnailFileName = removeExtension(savedFileName) + "_s." + extension;
        thumbnailPath = thumbnailPath + "/" + thumbnailFileName;

        try {
            if (thumbnailWidth != 0 && thumbnailHeight != 0) {
                Thumbnails.of(imagePath)
                        .width(thumbnailWidth)
                        .height(thumbnailHeight)
                        .toFile(thumbnailPath);
            } else if (thumbnailWidth != 0) {
                Thumbnails.of(imagePath)
                        .width(thumbnailWidth)
                        .toFile(thumbnailPath);
            } else if (thumbnailHeight != 0) {
                Thumbnails.of(imagePath)
                        .height(thumbnailHeight)
                        .toFile(thumbnailPath);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return thumbnailFileName;
    }

    public static boolean isImageFile(String contentType) {
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"));
    }
}

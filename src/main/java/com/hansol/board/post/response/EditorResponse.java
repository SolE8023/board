package com.hansol.board.post.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorResponse {
    private String filename;
    private int uploaded;
    private String url;

    @Builder
    public EditorResponse(String filename, int uploaded, String url) {
        this.filename = filename;
        this.uploaded = uploaded;
        this.url = url;
    }
}

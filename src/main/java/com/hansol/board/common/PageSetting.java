package com.hansol.board.common;

import com.hansol.board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageSetting {
    private static final int PAGINATION_SIZE = 10;
    private static final int POSTS_PER_PAGE = 15;
    public static Pageable getPostPageable(Integer page) {
        Sort.Order orderByCreatedDate = Sort.Order.desc("createdDate");
        Sort.Order orderBySecret = Sort.Order.desc("secret");

        Sort sort = Sort.by(orderByCreatedDate, orderBySecret);
        return PageRequest.of(page, POSTS_PER_PAGE, sort);
    }
}

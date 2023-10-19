package com.hansol.board.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

@Slf4j
public class PageSetting {
    public static final int PAGINATION_SIZE = 10;
    public static final int POSTS_PER_PAGE = 15;
    public static Pageable getPostPageable(Integer page) {
        Sort.Order orderByCreatedDate = Sort.Order.desc("createdDate");
        Sort.Order orderBySecret = Sort.Order.desc("secret");

        Sort sort = Sort.by(orderByCreatedDate, orderBySecret);
        return PageRequest.of(page, PAGINATION_SIZE, sort);
    }

    public static <T> int getPrevPage(Page<T> list) {
        int prevPage = getStartPage(list);
        if(prevPage != 0) return prevPage-1;
        else return 0;
    }

    public static <T> int getNextPage(Page<T> list) {
        int nextPage = getEndPage(list);

        if(list.getTotalPages() == 0) return 0;
        else if(nextPage > list.getTotalPages() - 1) return list.getTotalPages() - 1;
        else return nextPage + 1;
    }

    public static <T> int getTotalPages(Page<T> list) {
        if(list.getTotalPages() == 0) return 0;
        else return list.getTotalPages() - 1;
    }

    public static <T> int getStartPage(Page<T> list) {
        int currentPage = 1;
        if(list.getNumber() != 0) currentPage = list.getNumber();
        int startPage = (currentPage/PageSetting.PAGINATION_SIZE) * PAGINATION_SIZE;
        if(startPage == 0) startPage = 1;
        return startPage;
    }

    public static <T> int getEndPage(Page<T> list) {
        int currentPage = 1;
        if(list.getNumber() != 0) currentPage = list.getNumber();
        int endPage = (currentPage/(PageSetting.PAGINATION_SIZE)) * PAGINATION_SIZE + PAGINATION_SIZE;
        if(endPage > list.getTotalPages() - 1) endPage = list.getTotalPages() - 1;
        if(list.getTotalPages() == 0) endPage = 1;
        return endPage;
    }

    public static <T> void setPageVariable(Model model, Page<T> list) {
        int prevPage = getPrevPage(list);
        int nextPage = getNextPage(list);
        int finalPage = getTotalPages(list);
        int startPage = getStartPage(list);
        int endPage = getEndPage(list);
        int startNumber = list.getNumber() * list.getSize() + 1;

        model.addAttribute("startNumber", startNumber);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("finalPage", finalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", list.getNumber());
    }
}

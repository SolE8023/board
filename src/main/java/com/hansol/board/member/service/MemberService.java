package com.hansol.board.member.service;

import com.hansol.board.member.domain.Member;

public interface MemberService {
    Member signUp(Member member);

    Member update(Member member, String password);

    void remove(Long id, String password);
}

package com.hansol.board.member.repository;

import com.hansol.board.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Member update(Member member, String password);

    Boolean passwordCheck(Long id, String password);

    Optional<Member> findById(Long id);

    void remove(Long id, String password);
}

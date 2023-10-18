package com.hansol.board.member.repository;

import com.hansol.board.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Long id);
}

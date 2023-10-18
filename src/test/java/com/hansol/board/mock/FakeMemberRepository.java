package com.hansol.board.mock;

import com.hansol.board.member.domain.Member;
import com.hansol.board.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {
    private Long id = 0L;
    private final List<Member> members = new ArrayList<>();
    @Override
    public Member save(Member member) {
        if (member.getId() == null || member.getId().equals(0L)) {
            member.setId(++id);
            members.add(member);
        } else {
            members.removeIf(m -> m.getId().equals(member.getId()));
            members.add(member);
        }
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return members.stream().filter(m -> m.getId().equals(id)).findAny();
    }
}

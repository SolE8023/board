package com.hansol.board.mock;

import com.hansol.board.exception.NoMemberException;
import com.hansol.board.exception.PasswordErrorException;
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
            throw new IllegalArgumentException("잘못된 memberId 입니다.");
        }
        return member;
    }

    @Override
    public Member update(Member member, String password) {
        if (passwordCheck(member.getId(), password)) {
            members.removeIf(m -> m.getId().equals(member.getId()));
            members.add(member);
        } else {
            throw new PasswordErrorException();
        }
        return member;
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        for (Member m : members) {
            if (m.getId().equals(id) && m.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.stream()
                .filter(m -> m.getId().equals(id))
                .findAny()
                .orElseThrow(NoMemberException::new));
    }

    @Override
    public void remove(Long id, String password) {
        if (passwordCheck(id, password)) {
            members.removeIf(m -> m.getId().equals(id) && m.getPassword().equals(password));
        } else {
            throw new PasswordErrorException();
        }
    }
}

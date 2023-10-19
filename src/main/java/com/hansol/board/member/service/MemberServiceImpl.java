package com.hansol.board.member.service;

import com.hansol.board.member.domain.Member;
import com.hansol.board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public Member signUp(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member update(Member member, String password) {
        return memberRepository.update(member, password);
    }

    @Override
    public void remove(Long id, String password) {
        memberRepository.remove(id, password);
    }
}

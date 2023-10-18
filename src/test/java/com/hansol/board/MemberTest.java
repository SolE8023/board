package com.hansol.board;

import com.hansol.board.member.domain.Member;
import com.hansol.board.mock.TestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class MemberTest {
    @Test
    void 회원_가입을_할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Member member = Member.builder()
                .memberId("test123")
                .name("홍길동")
                .password("qwer1234")
                .zipCode(32132)
                .address1("경상남도 창원시 의창구 명서동 123")
                .address2("스카이빌 202호")
                .lastLogin(LocalDateTime.of(2023, 8, 1, 15, 11))
                .build();

        //when
        Member saved = testContainer.memberRepository.save(member);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 회원정보_수정을_할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Member member1 = Member.builder()
                .memberId("test123")
                .name("홍길동")
                .password("qwer1234")
                .zipCode(32132)
                .address1("경상남도 창원시 의창구 명서동 123")
                .address2("스카이빌 202호")
                .lastLogin(LocalDateTime.of(2023, 8, 1, 15, 11))
                .build();
        Member saved = testContainer.memberRepository.save(member1);

        //when
        Member member2 = Member.builder()
                .id(saved.getId())
                .memberId("test321")
                .name("황진이")
                .password("qwer4321")
                .zipCode(12312)
                .build();
        Member updated = testContainer.memberRepository.save(member2);

        //then
        Optional<Member> findMember = testContainer.memberRepository.findById(saved.getId());
        assertThat(findMember.isPresent()).isTrue();
        assertThat(findMember.get().getMemberId()).isEqualTo("test321");
        assertThat(findMember.get().getName()).isEqualTo("황진이");
        assertThat(findMember.get().getPassword()).isEqualTo("qwer4321");
        assertThat(findMember.get().getZipCode()).isEqualTo(12312);
    }

    @Test
    void 회원정보_수정시에_비밀번호가_맞아야_한다() {
        assertThat(false).isTrue();
    }

    @Test
    void 회원정보_삭제시에_비밀번호가_맞아야_한다() {
        assertThat(false).isTrue();
    }
}

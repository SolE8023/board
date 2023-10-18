package com.hansol.board.member;

import com.hansol.board.exception.NoMemberException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.member.domain.Member;
import com.hansol.board.mock.TestContainer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class MemberRepositoryTest {
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
        Member updated = testContainer.memberRepository.update(member2, "qwer1234");

        //then
        Optional<Member> findMember = testContainer.memberRepository.findById(saved.getId());
        assertThat(findMember.isPresent()).isTrue();
        assertThat(findMember.get().getMemberId()).isEqualTo(updated.getMemberId());
        assertThat(findMember.get().getName()).isEqualTo(updated.getName());
        assertThat(findMember.get().getPassword()).isEqualTo(updated.getPassword());
        assertThat(findMember.get().getZipCode()).isEqualTo(updated.getZipCode());
    }

    @Test
    void 회원정보_수정시에_비밀번호가_맞지_않으면_예외를_던진다() {
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

        //then
        assertThatThrownBy(() -> testContainer.memberRepository.update(member2, "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }

    @Test
    void 회원정보_삭제시에_비밀번호가_맞아야_한다() {
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
        Member saved = testContainer.memberRepository.save(member);

        //when
        testContainer.memberRepository.remove(member.getId(), "qwer1234");

        //then
        assertThatThrownBy(() -> testContainer.memberRepository.findById(member.getId()))
                .isInstanceOf(NoMemberException.class);
    }

    @Test
    void 회원정보_삭제시에_비밀번호가_맞지_않으면_예외를_던진다() {
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
        Member saved = testContainer.memberRepository.save(member);

        //when
        //then
        assertThatThrownBy(() -> testContainer.memberRepository.remove(member.getId(), "wrong password"))
                .isInstanceOf(PasswordErrorException.class);
    }
}

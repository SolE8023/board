package com.hansol.board.member;

import com.hansol.board.exception.NoMemberException;
import com.hansol.board.member.domain.Member;
import com.hansol.board.mock.TestContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest {
    @Test
    void 회원_가입이_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Member member = Member.builder()
                .memberId("test123")
                .password("qwer1234")
                .name("홍길동")
                .zipCode(32132)
                .address1("경상남도 창원시 의창구 명서동 123")
                .address2("스카이빌 202호")
                .build();

        //when
        Member saved = testContainer.memberService.signUp(member);

        //then
        assertThat(saved.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(saved.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    void 회원_정보_수정이_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Member member = Member.builder()
                .memberId("test123")
                .password("qwer1234")
                .name("홍길동")
                .zipCode(32132)
                .address1("경상남도 창원시 의창구 명서동 123")
                .address2("스카이빌 202호")
                .build();
        Member saved = testContainer.memberService.signUp(member);

        Member update = Member.builder()
                .id(saved.getId())
                .memberId("test111")
                .password("777")
                .name("황진이")
                .zipCode(12345)
                .address1("경상남도 창원시 의창구 명서동 321")
                .address2("스카이빌 777호")
                .build();

        //when
        Member updated = testContainer.memberService.update(update, "qwer1234");

        //then
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getMemberId()).isEqualTo(update.getMemberId());
        assertThat(updated.getPassword()).isEqualTo(update.getPassword());
        assertThat(updated.getName()).isEqualTo(update.getName());
        assertThat(updated.getZipCode()).isEqualTo(update.getZipCode());
        assertThat(updated.getAddress1()).isEqualTo(update.getAddress1());
        assertThat(updated.getAddress2()).isEqualTo(update.getAddress2());
    }

    @Test
    void 회원_탈퇴가_가능해야_한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        Member member = Member.builder()
                .memberId("test123")
                .password("qwer1234")
                .name("홍길동")
                .zipCode(32132)
                .address1("경상남도 창원시 의창구 명서동 123")
                .address2("스카이빌 202호")
                .build();
        Member saved = testContainer.memberService.signUp(member);

        //when
        testContainer.memberService.remove(saved.getId(), "qwer1234");

        //then
        assertThatThrownBy(() -> testContainer.memberRepository.findById(saved.getId()))
                .isInstanceOf(NoMemberException.class);
    }
}

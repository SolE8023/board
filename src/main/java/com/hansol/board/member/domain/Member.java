package com.hansol.board.member.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Member {
    private Long id;
    private String memberId;
    private String name;
    private String password;
    private Integer zipCode;
    private String address1;
    private String address2;
    private LocalDateTime lastLogin;

    @Builder
    public Member(Long id, String memberId, String name, String password, int zipCode, String address1, String address2, LocalDateTime lastLogin) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.password = password;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.lastLogin = lastLogin;
    }
}

package com.back.domain.member.member.dto;

import com.back.domain.member.member.entity.Member;

import java.time.LocalDateTime;

public record MemberWtihtUsernameDto(
        long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String nickname,
        String username
) {

    public MemberWtihtUsernameDto(Member member) {
        this(
                member.getId(),
                member.getCreateDate(),
                member.getModifyDate(),
                member.getNickname(),
                member.getUsername()
        );
    }
}


package com.wanchcoach.domain.member.service;

import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.domain.member.service.dto.MemberSignupDto;
import com.wanchcoach.global.api.ApiResult;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ApiResult<Member> signup(MemberSignupDto memberSignupDto) {
        return ApiResult.OK(memberRepository.save(memberSignupDto.toEntity()));
    }

    public ApiResult<Boolean> idDuplicateCheck(String id) {
        return ApiResult.OK(memberRepository.findByLoginId(id) == null);
    }
}

package com.wanchcoach.domain.member.controller.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberUpdateInfoRequest {
    String name;
    String email;
    LocalDate birthDate;
    String gender;
    String phoneNumber;
}

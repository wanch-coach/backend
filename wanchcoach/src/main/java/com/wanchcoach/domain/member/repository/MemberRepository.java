package com.wanchcoach.domain.member.repository;


import com.wanchcoach.domain.member.entity.DrugAdministrationTime;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.service.dto.FindMemberLoginIdDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByLoginIdAndEncryptedPwd(String id, String pwd);

    Member findByMemberId(Long memberId);


    boolean existsByLoginId(String loginId);

    Member findByNameAndPhoneNumberAndBirthDate(String name, String s, LocalDate localDate);

    Member findByNameAndLoginIdAndPhoneNumberAndBirthDate(String name, String loginId, String phoneNumber, LocalDate birthDate);
}

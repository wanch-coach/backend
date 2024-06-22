package com.wanchcoach.domain.member.repository;


import com.wanchcoach.domain.member.entity.DrugAdministrationTime;
import com.wanchcoach.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByLoginIdAndEncryptedPwd(String id, String pwd);

    Member findByMemberId(Long memberId);
}

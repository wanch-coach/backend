package com.wanchcoach.domain.member.repository;

import com.wanchcoach.domain.member.entity.DrugAdministrationTime;
import com.wanchcoach.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DrugAdministrationTimeRepository extends JpaRepository<DrugAdministrationTime, Member> {

    DrugAdministrationTime findByMemberMemberId(Long memberId);
    DrugAdministrationTime save(DrugAdministrationTime drugAdministrationTime);
}

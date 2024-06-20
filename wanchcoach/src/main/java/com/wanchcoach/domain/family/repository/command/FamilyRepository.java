package com.wanchcoach.domain.family.repository.command;

import com.wanchcoach.domain.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family,Long> {
    List<Family> findAllByMemberMemberId(Long memberId);
}

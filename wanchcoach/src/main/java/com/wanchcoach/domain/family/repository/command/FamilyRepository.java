package com.wanchcoach.domain.family.repository.command;

import com.wanchcoach.domain.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family,Long> {
}

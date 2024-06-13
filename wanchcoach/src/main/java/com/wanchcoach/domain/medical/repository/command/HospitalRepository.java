package com.wanchcoach.domain.medical.repository.command;

import com.wanchcoach.domain.medical.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}

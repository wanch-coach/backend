package com.wanchcoach.domain.medical.repository.command;

import com.wanchcoach.domain.medical.entity.HospitalOpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalOpeningHourRepository extends JpaRepository<HospitalOpeningHour, Long> {
}

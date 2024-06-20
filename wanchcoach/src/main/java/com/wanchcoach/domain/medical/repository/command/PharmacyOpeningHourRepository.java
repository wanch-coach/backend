package com.wanchcoach.domain.medical.repository.command;

import com.wanchcoach.domain.medical.entity.PharmacyOpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyOpeningHourRepository extends JpaRepository<PharmacyOpeningHour, Long> {
}

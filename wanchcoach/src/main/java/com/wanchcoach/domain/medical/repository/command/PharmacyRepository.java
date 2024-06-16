package com.wanchcoach.domain.medical.repository.command;

import com.wanchcoach.domain.medical.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}

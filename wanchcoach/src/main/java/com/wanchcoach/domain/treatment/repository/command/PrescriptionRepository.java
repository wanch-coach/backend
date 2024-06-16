package com.wanchcoach.domain.treatment.repository.command;

import com.wanchcoach.domain.treatment.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}

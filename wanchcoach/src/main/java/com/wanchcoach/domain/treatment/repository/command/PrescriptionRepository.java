package com.wanchcoach.domain.treatment.repository.command;

import com.wanchcoach.domain.treatment.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findByPrescriptionId(Long prescriptionId);

}

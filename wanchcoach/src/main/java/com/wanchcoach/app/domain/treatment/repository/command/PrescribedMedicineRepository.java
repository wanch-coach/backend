package com.wanchcoach.app.domain.treatment.repository.command;

import com.wanchcoach.app.domain.treatment.entity.PrescribedMedicine;
import com.wanchcoach.app.domain.treatment.entity.PrescribedMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescribedMedicineRepository extends JpaRepository<PrescribedMedicine, PrescribedMedicineId> {
}

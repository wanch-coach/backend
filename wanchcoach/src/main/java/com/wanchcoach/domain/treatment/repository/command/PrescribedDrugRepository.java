package com.wanchcoach.domain.treatment.repository.command;

import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.entity.PrescribedDrugId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescribedDrugRepository extends JpaRepository<PrescribedDrug, PrescribedDrugId> {
}

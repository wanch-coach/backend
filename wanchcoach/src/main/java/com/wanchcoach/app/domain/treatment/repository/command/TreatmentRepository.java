package com.wanchcoach.app.domain.treatment.repository.command;

import com.wanchcoach.app.domain.treatment.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 진료 CRUD 레포지토리
 *
 * @author 박은규
 */

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}
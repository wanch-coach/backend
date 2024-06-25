package com.wanchcoach.domain.medication.repository;

import com.wanchcoach.domain.medication.entity.MedicineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRecordRepository extends JpaRepository<MedicineRecord, Long> {
}

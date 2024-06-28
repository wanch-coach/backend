package com.wanchcoach.domain.drug.repository;

import com.wanchcoach.domain.drug.entity.DrugImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugImageRepository extends JpaRepository<DrugImage, Long> {
}

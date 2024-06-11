package com.wanchcoach.app.domain.drug.repository;

import com.wanchcoach.app.domain.drug.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

}

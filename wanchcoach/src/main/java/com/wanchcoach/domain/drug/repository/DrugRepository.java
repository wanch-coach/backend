package com.wanchcoach.domain.drug.repository;

import com.wanchcoach.domain.drug.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    Optional<Drug> findByItemSeq(long itemSeq);
}

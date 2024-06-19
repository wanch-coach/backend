package com.wanchcoach.domain.drug.repository;

import com.wanchcoach.domain.drug.entity.FavoriteDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteDrugRepository extends JpaRepository<FavoriteDrug, Long> {

}

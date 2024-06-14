package com.wanchcoach.domain.drug.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.drug.entity.Drug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.wanchcoach.domain.drug.entity.QDrug.drug;

/**
 * 약 조회 레포지토리
 *
 * @author 박은규
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class DrugQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 약 ID로 약 조회 쿼리
     *
     * @param drugId 약 ID
     * @return 해당 병원
     */
    public Drug findById(Long drugId) {
        return queryFactory
                .selectFrom(drug)
                .where(drug.drugId.eq(drugId))
                .fetchOne();
    }
}

package com.wanchcoach.domain.medical.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.medical.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.wanchcoach.domain.medical.entity.QPharmacy.pharmacy;

/**
 * 약국 조회 레포지토리
 *
 * @author 박은규
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class PharmacyQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 약국 ID로 약국 조회 쿼리
     *
     * @param pharmacyId 약국 ID
     * @return 해당 병원
     */
    public Pharmacy findById(Long pharmacyId) {
        return queryFactory
                .selectFrom(pharmacy)
                .where(pharmacy.id.eq(pharmacyId))
                .fetchOne();
    }
}

package com.wanchcoach.app.domain.treatment.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.app.domain.treatment.entity.Prescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.wanchcoach.app.domain.treatment.entity.QPrescription.prescription;

/**
 * 처방전 조회 레포지토리
 *
 * @author 박은규
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class PrescriptionQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 처방전 ID로 처방전 조회 쿼리
     *
     * @param prescriptionId 처방전 ID
     * @return 해당 처방전
     */
    public Prescription findById(Long prescriptionId) {
        return queryFactory
                .selectFrom(prescription)
                .where(prescription.id.eq(prescriptionId))
                .fetchOne();
    }
}

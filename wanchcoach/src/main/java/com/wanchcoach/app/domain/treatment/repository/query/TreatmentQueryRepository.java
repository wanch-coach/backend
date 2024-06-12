package com.wanchcoach.app.domain.treatment.repository.query;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.app.domain.treatment.entity.Treatment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.wanchcoach.app.domain.treatment.entity.QTreatment.treatment;

/**
 * 진료 조회 레포지토리
 *
 * @author 박은규
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class TreatmentQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 처방전 ID로 처방전 조회 쿼리
     *
     * @param treatmentId 진료 ID
     * @return 해당 처방전
     */
    public Treatment findById(Long treatmentId) {
        return queryFactory
                .selectFrom(treatment)
                .where(treatment.id.eq(treatmentId))
                .fetchOne();
    }

    /**
     * 처방전 ID로 가족 ID 조회 쿼리
     *
     * @param prescriptionId 처방전 ID
     * @return 가족 ID
     */
    public Long findFamilyIdByPrescriptionId(Long prescriptionId) {
//        return queryFactory
//                .select(treatment.familyId)
//                .from(treatment)
//                .where(treatment.prescription.id.eq(prescriptionId))
//                .fetchOne();
        return null;
    }
}

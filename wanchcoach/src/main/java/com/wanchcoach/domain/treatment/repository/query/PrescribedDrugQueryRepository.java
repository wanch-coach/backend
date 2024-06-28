package com.wanchcoach.domain.treatment.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanchcoach.domain.treatment.entity.QPrescribedDrug.prescribedDrug;

/**
 * 처방받은 약 조회 레포지토리
 *
 * @author 박은규
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class PrescribedDrugQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 처방전 ID로 처방받은 약 리스트 조회 쿼리
     * @return 처방받은 약 리스트
     */
    public List<PrescribedDrug> findByPrescriptionId(Long prescriptionId) {
        return queryFactory
                .selectFrom(prescribedDrug)
                .where(prescribedDrug.prescription.prescriptionId.eq(prescriptionId))
                .fetch();
    }
}

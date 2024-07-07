package com.wanchcoach.domain.treatment.repository.query;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.treatment.controller.dto.response.TreatmentItem;
import com.wanchcoach.domain.treatment.entity.Treatment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.wanchcoach.domain.treatment.entity.QTreatment.treatment;

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
     * 진료 ID로 진료 조회 쿼리
     *
     * @param treatmentId 진료 ID
     * @return 해당 진료
     */
    public Treatment findById(Long treatmentId) {
        return queryFactory
                .selectFrom(treatment)
                .where(treatment.treatmentId.eq(treatmentId))
                .fetchOne();
    }

    /**
     * 처방전 ID로 진료 조회 쿼리
     *
     * @param prescriptionId 처방전 ID
     * @return 해당 진료
     */
    public Treatment findByPrescriptionId(Long prescriptionId) {
        return queryFactory
                .selectFrom(treatment)
                .where(treatment.prescription.prescriptionId.eq(prescriptionId))
                .fetchOne();
    }

    /**
     * 회원 ID로 전체 가족의 진료 조회 쿼리
     *
     * @param memberId 회원 ID
     * @param isUpcoming 예정된 진료 여부
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findTreatments(Long memberId, Boolean isUpcoming) {
        if (isUpcoming) {
            return queryFactory
                    .select(Projections.constructor(TreatmentItem.class,
                            treatment.treatmentId,
                            treatment.family.familyId,
                            treatment.family.name,
                            treatment.family.color,
                            treatment.hospital.hospitalId,
                            treatment.hospital.name,
                            treatment.prescription.prescriptionId,
                            treatment.department,
                            treatment.date,
                            treatment.taken,
                            treatment.alarm,
                            treatment.symptom))
                    .from(treatment)
                    .where(treatment.family.member.memberId.eq(memberId),
                            treatment.date.goe(LocalDateTime.now()),
                            treatment.active.eq(true)
                    )
                    .orderBy(treatment.date.desc())
                    .fetch();
        } else {
            return queryFactory
                    .select(Projections.constructor(TreatmentItem.class,
                            treatment.treatmentId,
                            treatment.family.familyId,
                            treatment.family.name,
                            treatment.family.color,
                            treatment.hospital.hospitalId,
                            treatment.hospital.name,
                            treatment.prescription.prescriptionId,
                            treatment.department,
                            treatment.date,
                            treatment.taken,
                            treatment.alarm,
                            treatment.symptom))
                    .from(treatment)
                    .where(treatment.family.member.memberId.eq(memberId),
                            treatment.date.lt(LocalDateTime.now()),
                            treatment.active.eq(true)
                    )
                    .orderBy(treatment.date.desc())
                    .fetch();
        }
    }

    /**
     * 가족 ID로 전체 진료 조회 쿼리
     *
     * @param familyId 가족 ID
     * @param isUpcoming 예정된 진료 여부
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findFamilyTreatments(Long familyId, Boolean isUpcoming) {
        if (isUpcoming) {
            return queryFactory
                    .select(Projections.constructor(TreatmentItem.class,
                            treatment.treatmentId,
                            treatment.family.familyId,
                            treatment.family.name,
                            treatment.family.color,
                            treatment.hospital.hospitalId,
                            treatment.hospital.name,
                            treatment.prescription.prescriptionId,
                            treatment.department,
                            treatment.date,
                            treatment.taken,
                            treatment.alarm,
                            treatment.symptom))
                    .from(treatment)
                    .where(treatment.family.familyId.eq(familyId),
                            treatment.date.goe(LocalDateTime.now()),
                            treatment.active.eq(true)
                    )
                    .orderBy(treatment.date.desc())
                    .fetch();
        } else {
            return queryFactory
                    .select(Projections.constructor(TreatmentItem.class,
                            treatment.treatmentId,
                            treatment.family.familyId,
                            treatment.family.name,
                            treatment.family.color,
                            treatment.hospital.hospitalId,
                            treatment.hospital.name,
                            treatment.prescription.prescriptionId,
                            treatment.department,
                            treatment.date,
                            treatment.taken,
                            treatment.alarm,
                            treatment.symptom))
                    .from(treatment)
                    .where(treatment.family.familyId.eq(familyId),
                            treatment.date.lt(LocalDateTime.now()),
                            treatment.active.eq(true)
                    )
                    .orderBy(treatment.date.desc())
                    .fetch();
        }
    }

    /**
     * 가족 ID 집합으로 진료 조회 쿼리
     *
     * @param familyIds 가족 ID 목록
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findTreatmentsByHospital(List<Long> familyIds) {
        return queryFactory
                .select(Projections.constructor(TreatmentItem.class,
                        treatment.treatmentId,
                        treatment.family.familyId,
                        treatment.family.name,
                        treatment.family.color,
                        treatment.hospital.hospitalId,
                        treatment.hospital.name,
                        treatment.prescription.prescriptionId,
                        treatment.department,
                        treatment.date,
                        treatment.taken,
                        treatment.alarm,
                        treatment.symptom))
                .from(treatment)
                .where(treatment.family.familyId.in(familyIds),
                        treatment.active.eq(true))
                .fetch();
    }

    /**
     * 가족 ID로 진료 조회 쿼리
     *
     * @param familyId 가족 ID
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findFamilyTreatmentsByHospital(Long familyId) {
        return queryFactory
                .select(Projections.constructor(TreatmentItem.class,
                        treatment.treatmentId,
                        treatment.family.familyId,
                        treatment.family.name,
                        treatment.family.color,
                        treatment.hospital.hospitalId,
                        treatment.hospital.name,
                        treatment.prescription.prescriptionId,
                        treatment.department,
                        treatment.date,
                        treatment.taken,
                        treatment.alarm,
                        treatment.symptom))
                .from(treatment)
                .where(treatment.family.familyId.eq(familyId),
                        treatment.active.eq(true))
                .fetch();
    }

    /**
     * 가족 ID 집합으로 진료 조회 쿼리
     *
     * @param familyIds 가족 ID 목록
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findTreatmentsByDate(List<Long> familyIds, Integer year, Integer month) {

        DateTemplate<String> dateTemplate = Expressions.dateTemplate(String.class, "DATE_FORMAT({0}, '%Y-%m')", treatment.date);

        return queryFactory
                .select(Projections.constructor(TreatmentItem.class,
                        treatment.treatmentId,
                        treatment.family.familyId,
                        treatment.family.name,
                        treatment.family.color,
                        treatment.hospital.hospitalId,
                        treatment.hospital.name,
                        treatment.prescription.prescriptionId,
                        treatment.department,
                        treatment.date,
                        treatment.taken,
                        treatment.alarm,
                        treatment.symptom))
                .from(treatment)
                .where(treatment.family.familyId.in(familyIds),
                        treatment.active.eq(true),
                        dateTemplate.eq(String.format("%d-%02d", year, month)))
                // treatment.date.year().eq(year),
                // treatment.date.month().eq(month)
                .fetch();
    }

    /**
     * 가족 ID 집합으로 진료 조회 쿼리
     *
     * @param familyIds 가족 ID 목록
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findTreatmentsByDate(List<Long> familyIds, Integer year, Integer month, Integer day) {

        DateTemplate<String> dateTemplate = Expressions.dateTemplate(String.class, "DATE_FORMAT({0}, '%Y-%m-%d')", treatment.date);

        return queryFactory
                .select(Projections.constructor(TreatmentItem.class,
                        treatment.treatmentId,
                        treatment.family.familyId,
                        treatment.family.name,
                        treatment.family.color,
                        treatment.hospital.hospitalId,
                        treatment.hospital.name,
                        treatment.prescription.prescriptionId,
                        treatment.department,
                        treatment.date,
                        treatment.taken,
                        treatment.alarm,
                        treatment.symptom))
                .from(treatment)
                .where(treatment.family.familyId.in(familyIds),
                        treatment.active.eq(true),
                        dateTemplate.eq(String.format("%d-%02d-%02d", year, month, day)))
                // treatment.date.year().eq(year),
                // treatment.date.month().eq(month)
                .fetch();
    }

    /**
     * 가족 ID 집합으로 진료 조회 쿼리
     *
     * @param familyId 가족 ID
     * @return 모든 진료 정보
     */
    public List<TreatmentItem> findFamilyTreatmentsByDate(Long familyId, Integer year, Integer month) {

        DateTemplate<String> dateTemplate = Expressions.dateTemplate(String.class, "DATE_FORMAT({0}, '%Y-%m')", treatment.date);

        return queryFactory
                .select(Projections.constructor(TreatmentItem.class,
                        treatment.treatmentId,
                        treatment.family.familyId,
                        treatment.family.name,
                        treatment.family.color,
                        treatment.hospital.hospitalId,
                        treatment.hospital.name,
                        treatment.prescription.prescriptionId,
                        treatment.department,
                        treatment.date,
                        treatment.taken,
                        treatment.alarm,
                        treatment.symptom))
                .from(treatment)
                .where(treatment.family.familyId.eq(familyId),
                        treatment.active.eq(true),
                        dateTemplate.eq(String.format("%d-%02d", year, month)))
                // treatment.date.year().eq(year),
                // treatment.date.month().eq(month)
                .fetch();
    }
}

package com.wanchcoach.domain.medical.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.medical.entity.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.wanchcoach.domain.medical.entity.QHospital.hospital;

/**
 * 병원 조회 레포지토리
 *
 * @author 박은규
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class HospitalQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 병원 ID로 병원 조회 쿼리
     *
     * @param hospitalId 병원 ID
     * @return 해당 병원
     */
    public Hospital findById(Long hospitalId) {
        return queryFactory
                .selectFrom(hospital)
                .where(hospital.hospitalId.eq(hospitalId))
                .fetchOne();
    }
}

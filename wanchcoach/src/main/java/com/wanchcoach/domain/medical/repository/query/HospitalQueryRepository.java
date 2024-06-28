package com.wanchcoach.domain.medical.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.medical.controller.dto.response.HospitalDetailItem;
import com.wanchcoach.domain.medical.controller.dto.response.HospitalItem;
import com.wanchcoach.domain.medical.controller.dto.response.OpeningHourItem;
import com.wanchcoach.domain.medical.entity.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.wanchcoach.domain.medical.entity.QHospital.hospital;
import static com.wanchcoach.domain.medical.entity.QHospitalOpeningHour.hospitalOpeningHour;

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
     * 병의원 ID로 병의원 조회 쿼리
     *
     * @param hospitalId 병의원 ID
     * @return 해당 병의원
     */
    public Hospital findById(Long hospitalId) {
        return queryFactory
                .selectFrom(hospital)
                .where(hospital.hospitalId.eq(hospitalId))
                .fetchOne();
    }

    /**
     * 병의원 ID로 상세 정보 조회 메서드
     *
     * @param hospitalId 병의원 ID
     * @param longitude 현위치 경도
     * @param latitude 현위치 위도
     * @return 병의원 상세 정보
     */
    public HospitalDetailItem findDetailById(Long hospitalId, BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, hospital.latitude, longitude, hospital.longitude);

        return queryFactory.from(hospital)
                .leftJoin(hospitalOpeningHour).on(hospital.hospitalId.eq(hospitalOpeningHour.hospital.hospitalId))
                .where(hospital.hospitalId.eq(hospitalId))
                .transform(GroupBy.groupBy(hospital.hospitalId).list(
                        Projections.constructor(HospitalDetailItem.class,
                                hospital.hospitalId,
                                hospital.name,
                                hospital.type,
                                hospital.address,
                                hospital.phoneNumber,
                                distance,
                                hospital.hasEmergencyRoom,
                                hospital.etc,
                                GroupBy.list(Projections.constructor(OpeningHourItem.class,
                                        hospitalOpeningHour.dayOfWeek,
                                        hospitalOpeningHour.startTime,
                                        hospitalOpeningHour.endTime))
                        )
                )).get(0);
    }

    /**
     * 키워드로 병원 조회 쿼리
     *
     * @param keyword 검색 키워드
     * @return 병원 정보
     */
    public List<HospitalItem> findByKeyword(String keyword, BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, hospital.latitude, longitude, hospital.longitude);

        return queryFactory.select(Projections.constructor(HospitalItem.class,
                        hospital.hospitalId,
                        hospital.name,
                        hospital.type,
                        hospital.address))
                .from(hospital)
                .where(hospital.name.containsIgnoreCase(keyword))
                .orderBy(distance.asc())
                .fetch();
    }

    /**
     * 키워드로 병의원 상세정보 조회 쿼리
     *
     * @param keyword 검색 키워드
     * @return 병의원 상세 정보
     */
    public List<HospitalDetailItem> findDetailsByKeyword(String keyword, BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, hospital.latitude, longitude, hospital.longitude);

        return queryFactory.from(hospital)
                .leftJoin(hospitalOpeningHour).on(hospital.hospitalId.eq(hospitalOpeningHour.hospital.hospitalId))
                .where(hospital.name.containsIgnoreCase(keyword))
                .orderBy(distance.asc())
                .transform(GroupBy.groupBy(hospital.hospitalId).list(
                        Projections.constructor(HospitalDetailItem.class,
                                hospital.hospitalId,
                                hospital.name,
                                hospital.type,
                                hospital.address,
                                hospital.phoneNumber,
                                distance,
                                hospital.hasEmergencyRoom,
                                hospital.etc,
                                GroupBy.list(Projections.constructor(OpeningHourItem.class,
                                        hospitalOpeningHour.dayOfWeek,
                                        hospitalOpeningHour.startTime,
                                        hospitalOpeningHour.endTime))
                        )
                ));
    }

    /**
     * 현위치 주변 병의원 조회 메서드
     *
     * @param longitude 현위치 경도
     * @param latitude 현위치 위도
     * @return 주변 병의원 정보
     */
    public List<HospitalDetailItem> findNearByHospitals(BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, hospital.latitude, longitude, hospital.longitude);

        List<Tuple> results = queryFactory
                .select(hospital.hospitalId, distance)
                .from(hospital)
                .where(distance.loe(BigDecimal.valueOf(3000)))
                .fetch();

        List<Long> ids = results.stream().map(tuple -> tuple.get(hospital.hospitalId)).toList();

        return queryFactory.from(hospital)
                .leftJoin(hospitalOpeningHour).on(hospital.hospitalId.eq(hospitalOpeningHour.hospital.hospitalId))
                .where(hospital.hospitalId.in(ids))
                .orderBy(distance.asc())
                .transform(GroupBy.groupBy(hospital.hospitalId).list(
                        Projections.constructor(HospitalDetailItem.class,
                                hospital.hospitalId,
                                hospital.name,
                                hospital.type,
                                hospital.address,
                                hospital.phoneNumber,
                                distance,
                                hospital.hasEmergencyRoom,
                                hospital.etc,
                                GroupBy.list(Projections.constructor(OpeningHourItem.class,
                                        hospitalOpeningHour.dayOfWeek,
                                        hospitalOpeningHour.startTime,
                                        hospitalOpeningHour.endTime))
                        )
                ));

    }

    /**
     * 두 지점 간 거리 계산 메서드
     *
     * @param currentLat 현위치 위도
     * @param targetLat 목적지 위도
     * @param currentLng 현위치 경도
     * @param targetLng 목적지 경도
     * @return 두 지점 간 거리
     */
    private NumberTemplate<BigDecimal> calculateDistance(BigDecimal currentLat, NumberPath<BigDecimal> targetLat,
                                                         BigDecimal currentLng, NumberPath<BigDecimal> targetLng) {
        return Expressions.numberTemplate(BigDecimal.class,
                "ST_DISTANCE_SPHERE(POINT({0}, {1}), POINT({2}, {3}))",
                currentLng, currentLat, targetLng, targetLat);
    }
}

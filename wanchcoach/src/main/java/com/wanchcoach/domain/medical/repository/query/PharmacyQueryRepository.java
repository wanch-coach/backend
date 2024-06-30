package com.wanchcoach.domain.medical.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.medical.controller.dto.response.OpeningHourItem;
import com.wanchcoach.domain.medical.controller.dto.response.PharmacyDetailItem;
import com.wanchcoach.domain.medical.controller.dto.response.PharmacyItem;
import com.wanchcoach.domain.medical.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.wanchcoach.domain.medical.entity.QPharmacy.pharmacy;
import static com.wanchcoach.domain.medical.entity.QPharmacyOpeningHour.pharmacyOpeningHour;

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
                .where(pharmacy.pharmacyId.eq(pharmacyId))
                .fetchOne();
    }

    /**
     * 약국 ID로 상세 정보 조회 메서드
     * 
     * @param pharmacyId 약국 ID
     * @param longitude 현위치 경도
     * @param latitude 현위치 위도
     * @return 약국 상세 정보
     */
    public PharmacyDetailItem findDetailById(Long pharmacyId, BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, pharmacy.latitude, longitude, pharmacy.longitude);

        return queryFactory.from(pharmacy)
                .leftJoin(pharmacyOpeningHour).on(pharmacy.pharmacyId.eq(pharmacyOpeningHour.pharmacy.pharmacyId))
                .where(pharmacy.pharmacyId.eq(pharmacyId))
                .transform(GroupBy.groupBy(pharmacy.pharmacyId).list(
                        Projections.constructor(PharmacyDetailItem.class,
                                pharmacy.pharmacyId,
                                pharmacy.name,
                                pharmacy.address,
                                pharmacy.phoneNumber,
                                distance,
                                pharmacy.latitude,
                                pharmacy.longitude,
                                pharmacy.etc,
                                GroupBy.list(Projections.constructor(OpeningHourItem.class,
                                        pharmacyOpeningHour.dayOfWeek,
                                        pharmacyOpeningHour.startTime,
                                        pharmacyOpeningHour.endTime)))
                )).get(0);
    }

    /**
     * 키워드로 약국 조회 쿼리
     *
     * @param keyword 검색 키워드
     * @return 약국 정보
     */
    public List<PharmacyItem> findByKeyword(String keyword, BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, pharmacy.latitude, longitude, pharmacy.longitude);

        return queryFactory.select(Projections.constructor(PharmacyItem.class,
                pharmacy.pharmacyId,
                pharmacy.name,
                pharmacy.address))
                .from(pharmacy)
                .where(pharmacy.name.containsIgnoreCase(keyword))
                .orderBy(distance.asc())
                .fetch();
    }

    /**
     * 키워드로 약국 상세정보 조회 쿼리
     *
     * @param keyword 검색 키워드
     * @return 약국 상세 정보
     */
    public List<PharmacyDetailItem> findDetailsByKeyword(String keyword, BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, pharmacy.latitude, longitude, pharmacy.longitude);

        return queryFactory.from(pharmacy)
                .leftJoin(pharmacyOpeningHour).on(pharmacy.pharmacyId.eq(pharmacyOpeningHour.pharmacy.pharmacyId))
                .where(pharmacy.name.containsIgnoreCase(keyword))
                .orderBy(distance.asc())
                .transform(GroupBy.groupBy(pharmacy.pharmacyId).list(
                        Projections.constructor(PharmacyDetailItem.class,
                                pharmacy.pharmacyId,
                                pharmacy.name,
                                pharmacy.address,
                                pharmacy.phoneNumber,
                                distance,
                                pharmacy.latitude,
                                pharmacy.longitude,
                                pharmacy.etc,
                                GroupBy.list(Projections.constructor(OpeningHourItem.class,
                                        pharmacyOpeningHour.dayOfWeek,
                                        pharmacyOpeningHour.startTime,
                                        pharmacyOpeningHour.endTime))
                        )
        ));

    }

    /**
     * 현위치 주변 약국 조회 메서드
     * 
     * @param longitude 현위치 경도
     * @param latitude 현위치 위도
     * @return 주변 약국 정보
     */
    public List<PharmacyDetailItem> findNearByPharmacies(BigDecimal longitude, BigDecimal latitude) {
        NumberTemplate<BigDecimal> distance = calculateDistance(latitude, pharmacy.latitude, longitude, pharmacy.longitude);

        List<Tuple> results = queryFactory
                .select(pharmacy.pharmacyId, distance)
                .from(pharmacy)
                .where(distance.loe(BigDecimal.valueOf(1000)))
                .fetch();

        List<Long> ids = results.stream().map(tuple -> tuple.get(pharmacy.pharmacyId)).toList();

        return queryFactory.from(pharmacy)
                .leftJoin(pharmacyOpeningHour).on(pharmacy.pharmacyId.eq(pharmacyOpeningHour.pharmacy.pharmacyId))
                .where(pharmacy.pharmacyId.in(ids))
                .orderBy(distance.asc())
                .transform(GroupBy.groupBy(pharmacy.pharmacyId).list(
                        Projections.constructor(PharmacyDetailItem.class,
                                pharmacy.pharmacyId,
                                pharmacy.name,
                                pharmacy.address,
                                pharmacy.phoneNumber,
                                distance,
                                pharmacy.latitude,
                                pharmacy.longitude,
                                pharmacy.etc,
                                GroupBy.list(Projections.constructor(OpeningHourItem.class,
                                        pharmacyOpeningHour.dayOfWeek,
                                        pharmacyOpeningHour.startTime,
                                        pharmacyOpeningHour.endTime))
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

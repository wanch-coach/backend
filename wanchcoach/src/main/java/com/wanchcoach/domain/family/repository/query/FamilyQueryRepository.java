package com.wanchcoach.domain.family.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.family.entity.Family;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.wanchcoach.domain.family.entity.QFamily.family;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FamilyQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     *
     * 가족 ID로 가족 조회 쿼리
     *
     * @param familyId 가족 ID
     * @return 해당 가족
     */
    public Family findById(Long familyId) {
        return queryFactory
                .selectFrom(family)
                .where(family.id.eq(familyId))
                .fetchOne();
    }

    public String findNameById(Long familyId) {
        return queryFactory
                .select(family.name)
                .from(family)
                .where(family.id.eq(familyId))
                .fetchOne();
    }
}

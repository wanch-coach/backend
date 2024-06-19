package com.wanchcoach.domain.family.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.family.entity.Family;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .where(family.familyId.eq(familyId))
                .fetchOne();
    }

    /**
     *
     * 가족 ID로 이름 조회 쿼리
     *
     * @param familyId 가족 ID
     * @return 해당 가족 이름
     */
    public String findNameById(Long familyId) {
        return queryFactory
                .select(family.name)
                .from(family)
                .where(family.familyId.eq(familyId))
                .fetchOne();
    }

    /**
     *
     * 회원 ID로 등록된 전체 가족 ID 조회
     *
     * @param memberId 회원 ID
     * @return 해당 회원에 등록된 가족 ID 목록
     */
    public List<Long> findFamilyIdsByMemberId(Long memberId) {
        return queryFactory
                .selectDistinct(family.familyId)
                .from(family)
                .where(family.member.memberId.eq(memberId))
                .fetch();
    }
}

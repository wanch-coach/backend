package com.wanchcoach.domain.treatment.service;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.repository.query.FamilyQueryRepository;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.domain.treatment.controller.dto.response.*;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.domain.treatment.entity.Treatment;
import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.repository.query.PrescribedDrugQueryRepository;
import com.wanchcoach.domain.treatment.repository.query.TreatmentQueryRepository;
import com.wanchcoach.global.error.InvalidAccessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 진료 조회 서비스
 * 
 * @author 박은규
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TreatmentQueryService {

    private final TreatmentQueryRepository treatmentQueryRepository;
    private final MemberRepository memberQueryRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final PrescribedDrugQueryRepository prescribedDrugQueryRepository;

    /**
     * 회원 진료 조회 메서드
     *
     * @param memberId 회원 ID
     * @return 계정에 등록된 모든 가족의 진료 정보
     */
    public TreatmentResponse getTreatments(Long memberId) {
        List<TreatmentItem> upcoming = treatmentQueryRepository.findTreatments(memberId, true);
        List<TreatmentItem> past = treatmentQueryRepository.findTreatments(memberId, false);
        return new TreatmentResponse(upcoming, past);
    }

    /**
     * 가족 진료 조회 메서드
     *
     * @param familyId 가족 ID
     * @return 계정에 등록된 모든 가족의 진료 정보
     */
    public TreatmentResponse getFamilyTreatments(Long memberId, Long familyId) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Family family = familyQueryRepository.findById(familyId);
        checkValidAccess(member, family);

        List<TreatmentItem> upcoming = treatmentQueryRepository.findFamilyTreatments(familyId, true);
        List<TreatmentItem> past = treatmentQueryRepository.findFamilyTreatments(familyId, false);

        return new TreatmentResponse(upcoming, past);
    }

    /**
     * 회원 진료 병원별 조회 메서드
     *
     * @param memberId 회원 ID
     * @return 계정에 등록된 모든 가족의 병원별 진료 정보
     */
    public TreatmentHospitalResponse getTreatmentsByHospital(Long memberId) {
        List<Long> familyIds = familyQueryRepository.findFamilyIdsByMemberId(memberId);
        List<TreatmentItem> items = treatmentQueryRepository.findTreatmentsByHospital(familyIds);
        List<TreatmentHospitalItem> hospitalItems = refineTreatmentItemsByHospital(items);

        return new TreatmentHospitalResponse(hospitalItems);
    }

    /**
     * 가족 진료 병원별 조회 메서드
     *
     * @param familyId 가족 ID
     * @return 가족의 병원별 진료 정보
     */
    public TreatmentHospitalResponse getFamilyTreatmentsByHospital(Long memberId, Long familyId) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Family family = familyQueryRepository.findById(familyId);
        checkValidAccess(member, family);

        List<TreatmentItem> items = treatmentQueryRepository.findFamilyTreatmentsByHospital(familyId);
        List<TreatmentHospitalItem> hospitalItems = refineTreatmentItemsByHospital(items);

        return new TreatmentHospitalResponse(hospitalItems);
    }

    /**
     * 진료 목록 병원별로 변환 메서드
     * 
     * @param items 진료 목록
     * @return 병원별로 그룹화한 진료 목록
     */
    private List<TreatmentHospitalItem> refineTreatmentItemsByHospital(List<TreatmentItem> items) {

        // 1. 진료 목록을 hospitalId로 grouping
        Map<Long, List<TreatmentItem>> groupedItems = items.stream()
                .collect(Collectors.groupingBy(TreatmentItem::getHospitalId));

        // 2. 진료 목록을 최신순(시간 내림차순)으로 정렬
        groupedItems.forEach((hospitalId, treatmentItemList) -> Collections.sort(treatmentItemList));

        // 3. 진료 횟수가 많은 병원 순으로 반환값 생성
        return groupedItems.entrySet().stream()
                .map(o -> new TreatmentHospitalItem(o.getKey(), o.getValue().get(0).getHospitalName(), o.getValue()))
                .sorted((o1, o2) -> o2.getTreatmentItems().size() - o1.getTreatmentItems().size())
                .collect(Collectors.toList());
    }

    /**
     * 회원 진료 월별 조회 메서드
     *
     * @param memberId 회원 ID
     * @return 계정에 등록된 모든 가족의 월별 진료 정보
     */
    public TreatmentDateResponse getTreatmentsByDate(Long memberId, Integer year, Integer month) {
        List<Long> familyIds = familyQueryRepository.findFamilyIdsByMemberId(memberId);
        List<TreatmentItem> items = treatmentQueryRepository.findTreatmentsByDate(familyIds, year, month);
        List<TreatmentDateItem> dateItems = refineTreatmentItemsByDate(items);

        return new TreatmentDateResponse(dateItems);
    }

    /**
     * 가족 진료 월별 조회 메서드
     *
     * @param familyId 가족 ID
     * @return 가족의 월별 진료 정보
     */
    public TreatmentDateResponse getFamilyTreatmentsByDate(Long memberId, Long familyId, Integer year, Integer month) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Family family = familyQueryRepository.findById(familyId);
        checkValidAccess(member, family);

        List<TreatmentItem> items = treatmentQueryRepository.findFamilyTreatmentsByDate(familyId, year, month);
        List<TreatmentDateItem> dateItems = refineTreatmentItemsByDate(items);

        return new TreatmentDateResponse(dateItems);
    }

    private List<TreatmentDateItem> refineTreatmentItemsByDate(List<TreatmentItem> items) {

        // 1. 진료 목록을 날짜별로 grouping
        Map<LocalDate, List<TreatmentItem>> groupedItems = items.stream()
                .collect(Collectors.groupingBy(item -> item.getDate().toLocalDate()));

        // 2. 진료 목록을 오래된 순(시간 오름차순)으로 정렬
        groupedItems.forEach((date, treatmentItemList) -> treatmentItemList.sort(Collections.reverseOrder()));

        // 3. 반환값 생성
        return groupedItems.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(o -> new TreatmentDateItem(o.getKey(), o.getValue()))
                .collect(Collectors.toList());
    }


    /**
     * 진료 상세 조회 메서드
     *
     * @param treatmentId 상세 조회할 진료 ID
     * @return 진료 상세 정보
     */
    public TreatmentDetailResponse getTreatmentDetail(Long memberId, Long treatmentId) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findById(treatmentId);
        checkValidAccess(member, treatment);

        Prescription prescription = treatment.getPrescription();
        if (prescription == null) return TreatmentDetailResponse.of(treatment);

        List<PrescribedDrug> prescribedDrugs = prescribedDrugQueryRepository.findByPrescriptionId(prescription.getPrescriptionId());
        return TreatmentDetailResponse.of(treatment, prescription, prescribedDrugs);
    }

    private void checkValidAccess(Member member, Family family) {
        if (!family.getMember().getMemberId().equals(member.getMemberId())) {
            throw new InvalidAccessException("잘못된 접근입니다.");
        }
    }

    private void checkValidAccess(Member member, Treatment treatment) {
        if (!treatment.getFamily().getMember().getMemberId().equals(member.getMemberId())) {
            throw new InvalidAccessException("잘못된 접근입니다.");
        }
    }

}

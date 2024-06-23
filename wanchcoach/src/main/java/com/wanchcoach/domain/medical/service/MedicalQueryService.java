package com.wanchcoach.domain.medical.service;

import com.wanchcoach.domain.medical.controller.dto.response.*;
import com.wanchcoach.domain.medical.repository.query.HospitalQueryRepository;
import com.wanchcoach.domain.medical.repository.query.PharmacyQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 병의원/약국 조회 서비스
 *
 * @author 박은규
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicalQueryService {

    private final HospitalQueryRepository hospitalQueryRepository;
    private final PharmacyQueryRepository pharmacyQueryRepository;

    public HospitalDetailResponse findHospitalDetail(long hospitalId, double lng, double lat) {
        HospitalDetailItem item = hospitalQueryRepository.findDetailById(hospitalId, BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        return new HospitalDetailResponse(item);
    }


    public PharmacyDetailResponse findPharmacyDetail(long pharmacyId, double lng, double lat) {
        PharmacyDetailItem item = pharmacyQueryRepository.findDetailById(pharmacyId, BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        return new PharmacyDetailResponse(item);
    }

    /**
     * 주변 병의원/약국 조회 메서드
     *
     * @param lng 경도
     * @param lat 위도
     * @return 병의원 목록
     */
    public MedicalDetailResponse findNearbyMedicals(double lng, double lat) {
        List<HospitalDetailItem> hospitalDetailItems = hospitalQueryRepository.findNearByHospitals(BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        List<PharmacyDetailItem> pharmacyDetailItems = pharmacyQueryRepository.findNearByPharmacies(BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        return new MedicalDetailResponse(hospitalDetailItems, pharmacyDetailItems);
    }

    /**
     * 병의원/약국 상세 검색 메서드
     *
     * @param keyword 검색 키워드
     * @return 전체 병의원/약국 상세정보
     */
    public MedicalDetailResponse searchMedicalDetails(String keyword, double lng, double lat) {
        List<HospitalDetailItem> hospitalDetailItems = hospitalQueryRepository.findDetailsByKeyword(keyword, BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        List<PharmacyDetailItem> pharmacyDetailItems = pharmacyQueryRepository.findDetailsByKeyword(keyword, BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        return new MedicalDetailResponse(hospitalDetailItems, pharmacyDetailItems);
    }

    /**
     * 병의원 검색 메서드
     *
     * @param keyword 검색 키워드
     * @return 병의원 정보
     */
    public SearchHospitalResponse searchHospital(String keyword, double lng, double lat) {
        List<HospitalItem> hospitalItems = hospitalQueryRepository.findByKeyword(keyword, BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        return new SearchHospitalResponse(hospitalItems);
    }

    /**
     * 약국 검색 메서드
     *
     * @param keyword 검색 키워드
     * @return 약국 정보
     */
    public SearchPharmacyResponse searchPharmacy(String keyword, double lng, double lat) {
        List<PharmacyItem> pharmacyItems = pharmacyQueryRepository.findByKeyword(keyword, BigDecimal.valueOf(lng), BigDecimal.valueOf(lat));
        return new SearchPharmacyResponse(pharmacyItems);
    }
}

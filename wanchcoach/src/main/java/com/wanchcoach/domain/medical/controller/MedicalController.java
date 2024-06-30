package com.wanchcoach.domain.medical.controller;

import com.wanchcoach.domain.medical.controller.dto.response.*;
import com.wanchcoach.domain.medical.service.MedicalQueryService;
import com.wanchcoach.domain.medical.service.MedicalService;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 병의원/약국 관련 API 컨트롤러
 *
 * @author 박은규
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medical")
public class MedicalController {

    private final MedicalService medicalService;
    private final MedicalQueryService medicalQueryService;

    /**
     * 병의원 데이터 요청 및 저장 API
     *
     */
    @GetMapping("/hospital")
    public ApiResult<GetHospitalDataResponse> getHospitalData() {
        log.info("MedicalController#getHospitalData called");

//        GetHospitalDataResponse response = medicalService.getHospitalData();
//
//        return ApiResult.OK(response);
        return null;
    }

    /**
     * 약국 데이터 요청 및 저장 API
     *
     */
    @GetMapping("/pharmacy")
    public ApiResult<GetPharmacyDataResponse> getPharmacyData() {
        log.info("MedicalController#getPharmacyData called");

        GetPharmacyDataResponse response = medicalService.getPharmacyData();
        return ApiResult.OK(response);
    }

    /**
     * 병의원 상세 정보 조회 API
     * 
     * @param hospitalId 병의원 ID
     * @param lng 현위치 경도
     * @param lat 현위치 위도
     * @return 병의원 상세정보
     */
    @GetMapping("/hospital/{hospitalId}")
    public ApiResult<HospitalDetailResponse> getHospitalDetail(@PathVariable Long hospitalId,
                                                           @RequestParam double lng,
                                                           @RequestParam double lat) {
        log.info("MedicalController#getHospitalDetail called");

        HospitalDetailResponse response = medicalQueryService.findHospitalDetail(hospitalId, lng, lat);
        return ApiResult.OK(response);
    }

    /**
     * 약국 상세 정보 조회 API
     * 
     * @param pharmacyId 약국 ID
     * @param lng 현위치 경도
     * @param lat 현위치 위도
     * @return 약국 상세정보
     */
    @GetMapping("/pharmacy/{pharmacyId}")
    public ApiResult<PharmacyDetailResponse> getPharmacyDetail(@PathVariable Long pharmacyId,
                                                               @RequestParam double lng,
                                                               @RequestParam double lat) {
        log.info("MedicalController#getPharmacyDetail called");

        PharmacyDetailResponse response = medicalQueryService.findPharmacyDetail(pharmacyId, lng, lat);
        return ApiResult.OK(response);
    }

    /**
     * 현위치 주변 병의원/약국 조회 API(공공데이터 API 활용)
     *
     * @param lng 현위치 경도
     * @param lat 현위치 위도
     */
    @GetMapping("/location-search-api")
    public ApiResult<MedicalDetailResponse> getNearbyMedicalsWithAPI(@RequestParam double lng,
                                                              @RequestParam double lat) {

//        MedicalDetailResponse response = medicalQueryService.findNearbyMedicalsWithAPI(lng, lat);
//        return ApiResult.OK(response);
        return null;
    }

    /**
     * 현위치 주변 병의원/약국 조회 API
     * @param lat 현위치 위도
     * @param lng 현위치 경도
     * @return 주변 병의원/약국 상세 정보
     */
    @GetMapping("/location-search")
    public ApiResult<MedicalDetailResponse> getNearbyMedicals(@RequestParam double lng,
                                                              @RequestParam double lat) {
        log.info("MedicalController#getNearbyMedicals called");

        MedicalDetailResponse response = medicalQueryService.findNearbyMedicals(lng, lat);
        return ApiResult.OK(response);
    }

    /**
     * 병의원/약국 검색 페이지에서 키워드 검색 API
     * @param keyword 검색어
     * @param lat 현위치 위도
     * @param lng 현위치 경도
     * @return 키워드를 이름에 포함하는 병의원/약국 상세 정보
     */
    @GetMapping("/detail")
    public ApiResult<MedicalDetailResponse> searchMedicalDetails(@RequestParam(name = "keyword") String keyword,
                                                                 @RequestParam(name = "lng") double lng,
                                                                 @RequestParam(name = "lat") double lat) {
        log.info("MedicalController#searchMedicalDetails called");
        log.info("keyword : `{}` ", keyword);
        MedicalDetailResponse response = medicalQueryService.searchMedicalDetails(keyword, lng, lat);
        return ApiResult.OK(response);
    }

    /**
     * 진료 등록 시 병의원 검색 API
     * @param keyword 검색어
     * @param lat 현위치 위도
     * @param lng 현위치 경도
     * @return 키워드를 이름에 포함하는 병의원 정보
     */
    @GetMapping("/hospitals")
    public ApiResult<SearchHospitalResponse> searchHospital(@RequestParam String keyword,
                                                            @RequestParam double lng,
                                                            @RequestParam double lat) {
        log.info("MedicalController#searchHospital called");

        SearchHospitalResponse response = medicalQueryService.searchHospital(keyword, lng, lat);
        return ApiResult.OK(response);
    }

    /**
     * 처방전 등록 시 약국 검색 API
     * @param keyword 검색어
     * @param lat 현위치 위도
     * @param lng 현위치 경도
     * @return 키워드를 이름에 포함하는 약국 정보
     */
    @GetMapping("/pharmacies")
    public ApiResult<SearchPharmacyResponse> searchPharmacy(@RequestParam String keyword,
                                                            @RequestParam double lng,
                                                            @RequestParam double lat) {
        log.info("MedicalController#searchPharmacy called");

        SearchPharmacyResponse response = medicalQueryService.searchPharmacy(keyword, lng, lat);
        return ApiResult.OK(response);
    }

}

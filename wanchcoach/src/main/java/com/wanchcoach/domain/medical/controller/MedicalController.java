package com.wanchcoach.domain.medical.controller;

import com.wanchcoach.domain.medical.controller.dto.response.GetHospitalDataResponse;
import com.wanchcoach.domain.medical.controller.dto.response.GetPharmacyDataResponse;
import com.wanchcoach.domain.medical.service.MedicalService;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 병의원 데이터 요청 및 저장 API
     *
     */
    @GetMapping("/hospital")

    public ApiResult<GetHospitalDataResponse> getHospitalData() {
        log.info("MedicalController#getHospitalData called");

        GetHospitalDataResponse response = medicalService.getHospitalData();

        return ApiResult.OK(response);
    }

    /**
     * 병의원 데이터 요청 및 저장 API
     *
     */
    @GetMapping("/pharmacy")

    public ApiResult<GetPharmacyDataResponse> getPharmacyData() {
        log.info("MedicalController#getPharmacyData called");

        GetPharmacyDataResponse response = medicalService.getPharmacyData();

        return ApiResult.OK(response);
    }
}

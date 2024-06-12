package com.wanchcoach.domain.treatment.controller;

import com.wanchcoach.domain.treatment.controller.request.CreatePrescriptionRequest;
import com.wanchcoach.domain.treatment.controller.request.CreateTreatmentRequest;
import com.wanchcoach.domain.treatment.controller.response.CreatePrescriptionResponse;
import com.wanchcoach.domain.treatment.controller.response.CreateTreatmentResponse;
import com.wanchcoach.domain.treatment.service.TreatmentService;
import com.wanchcoach.domain.treatment.service.dto.CreatePrescriptionDto;
import com.wanchcoach.domain.treatment.service.dto.CreateTreatmentDto;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 진료 관련 API 컨트롤러
 *
 * @author 박은규
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/treatment")
public class TreatmentController {

    private final TreatmentService treatmentService;

    /**
     * 진료 등록 API
     *
     * @param treatmentRequest 등록할 진료 정보
     * @return 등록된 진료 정보
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<CreateTreatmentResponse> createTreatment(@RequestPart CreateTreatmentRequest treatmentRequest,
                                                              @RequestPart MultipartFile file){
        log.debug("TreatmentController#createTreatment called");

        // 처방전(복약) 정보 저장
            // 1. 약국 id + 사진파일만 저장하고 처방전 id 가져옴
            // todo: 처방전 이미지 파일 저장
        CreatePrescriptionRequest prescriptionRequest = treatmentRequest.prescription();
        CreatePrescriptionResponse prescriptionResponse = null;
        if (prescriptionRequest != null) {
            prescriptionResponse = treatmentService.createPrescription(CreatePrescriptionDto.of(
                    prescriptionRequest.pharmacyId(),
                    prescriptionRequest.morning(),
                    prescriptionRequest.noon(),
                    prescriptionRequest.evening(),
                    prescriptionRequest.beforeBed(),
                    prescriptionRequest.prescribedMedicines()), null, file
            );
        }

        // 진료 정보 저장
        // treatmentRequest -> dto로 변환
        CreateTreatmentResponse treatmentResponse = treatmentService.createTreatment(CreateTreatmentDto.of(
                treatmentRequest.familyId(),
                treatmentRequest.hospitalId(),
                prescriptionResponse != null ? prescriptionResponse.prescriptionId() : null,
                treatmentRequest.department(),
                treatmentRequest.date(),
                treatmentRequest.taken(),
                treatmentRequest.alarm(),
                treatmentRequest.symptom())
        );

        // response로 변환
        return ApiResult.OK(treatmentResponse);
    }

    /**
     * 처방전 등록 API
     *
     * @param treatmentId 진료 ID
     * @param prescriptionRequest 등록할 처방전 정보
     * @param file 처방전 이미지 파일
     * @return 등록한 처방전 ID
     */

    @PostMapping("/{treatmentId}/prescription")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<CreatePrescriptionResponse> createPrescription(@PathVariable Long treatmentId,
                                                                    @RequestPart CreatePrescriptionRequest prescriptionRequest,
                                                                    @RequestPart(required = false) MultipartFile file) {
        log.debug("TreatmentController#createPrescription called");

        // todo 이메일 정보 가져와서 계정 - 가족 정보 유효한지 확인

        CreatePrescriptionResponse response = treatmentService.createPrescription(CreatePrescriptionDto.of(
                prescriptionRequest.pharmacyId(),
                prescriptionRequest.morning(),
                prescriptionRequest.noon(),
                prescriptionRequest.evening(),
                prescriptionRequest.beforeBed(),
                prescriptionRequest.prescribedMedicines()), treatmentId, file
        );

        return ApiResult.OK(response);
    }
}

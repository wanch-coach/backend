package com.wanchcoach.domain.treatment.controller;

import com.wanchcoach.domain.treatment.controller.dto.request.CreatePrescriptionRequest;
import com.wanchcoach.domain.treatment.controller.dto.request.CreateTreatmentRequest;
import com.wanchcoach.domain.treatment.controller.dto.request.UpdateTreatmentRequest;
import com.wanchcoach.domain.treatment.controller.dto.response.*;
import com.wanchcoach.domain.treatment.service.TreatmentQueryService;
import com.wanchcoach.domain.treatment.service.TreatmentService;
import com.wanchcoach.domain.treatment.service.dto.*;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    private final TreatmentQueryService treatmentQueryService;
    private final String urlPrefix = "https://objectstorage.ap-chuncheon-1.oraclecloud.com";

    /**
     * 진료 등록 API
     *
     * @param treatmentRequest 등록할 진료 정보
     * @return 등록된 진료 정보
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<CreateTreatmentResponse> createTreatment(@RequestPart CreateTreatmentRequest treatmentRequest,
                                                              @RequestPart(required = false) MultipartFile file,
                                                              @AuthenticationPrincipal User user) throws Exception {
        log.info("TreatmentController#createTreatment called");

        Long memberId = Long.valueOf(user.getUsername());

        // 진료 정보 저장
        CreateTreatmentResponse treatmentResponse = treatmentService.createTreatment(memberId, CreateTreatmentDto.of(treatmentRequest));

        // 처방전(복약) 정보 저장
        CreatePrescriptionRequest prescriptionRequest = treatmentRequest.prescription();

        if (prescriptionRequest != null) {
            CreatePrescriptionResponse prescriptionResponse = treatmentService.createPrescription(
                    memberId, CreatePrescriptionDto.of(prescriptionRequest), treatmentResponse.treatmentId(), file
            );

            treatmentResponse = new CreateTreatmentResponse(treatmentResponse.treatmentId(), prescriptionResponse.prescriptionId());
        }

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
                                        @RequestPart(required = false) MultipartFile file,
                                        @AuthenticationPrincipal User user) throws Exception {
        log.info("TreatmentController#createPrescription called");

        Long memberId = Long.valueOf(user.getUsername());
        CreatePrescriptionResponse response = treatmentService.createPrescription(memberId, CreatePrescriptionDto.of(prescriptionRequest), treatmentId, file);

        return ApiResult.OK(response);
    }

    /**
     * 전체 가족 진료 조회 API
     *
     * @return 전체 가족 진료 정보
     */
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentResponse> getTreatments(@AuthenticationPrincipal User user) {
        log.info("TreatmentController#getTreatments called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentResponse response = treatmentQueryService.getTreatments(memberId);

        return ApiResult.OK(response);
    }

    /**
     * 가족 진료 조회 API
     *
     * @param familyId 조회할 가족 ID
     * @return 가족 진료 정보
     */
    @GetMapping("/families/{familyId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentResponse> getFamilyTreatments(@PathVariable Long familyId, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#getTreatmentByFamilyId called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentResponse response = treatmentQueryService.getFamilyTreatments(memberId, familyId);

        return ApiResult.OK(response);
    }

    /**
     * 전체 가족 진료 병원별 조회 API
     *
     * @return 전체 가족 병원별 진료 정보
     */
    @GetMapping("/hospitals")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentHospitalResponse> getTreatmentsByHospital(@AuthenticationPrincipal User user) {
        log.info("TreatmentController#getTreatmentsByHospital called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentHospitalResponse response = treatmentQueryService.getTreatmentsByHospital(memberId);

        return ApiResult.OK(response);
    }

    /**
     * 가족 진료 병원별 조회 API
     *
     * @param familyId 조회할 가족 ID
     * @return 가족 병원별 진료 정보
     */
    @GetMapping("/hospitals/families/{familyId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentHospitalResponse> getFamilyTreatmentsByHospital(@PathVariable Long familyId, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#getFamilyTreatmentsByHospital called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentHospitalResponse response = treatmentQueryService.getFamilyTreatmentsByHospital(memberId, familyId);

        return ApiResult.OK(response);
    }

    /**
     * 전체 가족 진료 월별 조회 API
     *
     * @param year 조회할 연도
     * @param month 조회할 달
     * @return 전체 가족 월별 진료 정보
     */
    @GetMapping("/date")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentDateResponse> getTreatmentsByDate(@RequestParam Integer year, @RequestParam Integer month, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#getTreatmentsByDate called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentDateResponse response = treatmentQueryService.getTreatmentsByDate(memberId, year, month);

        return ApiResult.OK(response);
    }

    /**
     * 가족 진료 월별 조회 API
     *
     * @param familyId 조회할 가족 ID
     * @param year 조회할 연도
     * @param month 조회할 달
     * @return 가족 월별 진료 정보
     */
    @GetMapping("/date/families/{familyId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentDateResponse> getFamilyTreatmentsByDate(@PathVariable Long familyId, @RequestParam Integer year, @RequestParam Integer month,
                                                                      @AuthenticationPrincipal User user) {
        log.info("TreatmentController#getFamilyTreatmentsByDate called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentDateResponse response = treatmentQueryService.getFamilyTreatmentsByDate(memberId, familyId, year, month);

        return ApiResult.OK(response);
    }

    /** 진료 상세 조회 API
     *
     * @param treatmentId 조회할 진료 ID
     * @return 조회할 진료 정보
     */
    @GetMapping("/{treatmentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentDetailResponse> getTreatmentDetail(@PathVariable Long treatmentId, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#getTreatmentDetail called");

        Long memberId = Long.valueOf(user.getUsername());
        TreatmentDetailResponse response = treatmentQueryService.getTreatmentDetail(memberId, treatmentId);

        return ApiResult.OK(response);
    }

    /**
     * 진료 알림 여부 변경
     * @param treatmentId 진료 ID
     * @return 알림 변경한 진료 ID 및 변경된 값
     */
    @PatchMapping("/alarm/{treatmentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<SetTreatmentAlarmResponse> setTreatmentAlarm(@PathVariable Long treatmentId, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#setTreatmentAlarm called");

        Long memberId = Long.valueOf(user.getUsername());
        SetTreatmentAlarmResponse response = treatmentService.setTreatmentAlarm(memberId, treatmentId);

        return ApiResult.OK(response);
    }

    /**
     * 진료 여부 변경
     * @param treatmentId 진료 ID
     * @return 변경한 진료 ID 및 변경된 값
     */
    @PatchMapping("/taken/{treatmentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TakeTreatmentResponse> takeTreatment(@PathVariable Long treatmentId, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#takeTreatment called");

        Long memberId = Long.valueOf(user.getUsername());
        TakeTreatmentResponse response = treatmentService.takeTreatment(memberId, treatmentId);

        return ApiResult.OK(response);
    }

    /**
     * 진료 정보 수정 API
     * @param treatmentId 수정할 진료 ID
     * @param treatmentRequest 수정할 정보
     * @return 수정한 진료 ID
     */
    @PatchMapping("/{treatmentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<UpdateTreatmentResponse> updateTreatment(@PathVariable Long treatmentId,
                                                              @RequestBody UpdateTreatmentRequest treatmentRequest,
                                                              @AuthenticationPrincipal User user) {
        log.info("TreatmentController#updateTreatment called");

        Long memberId = Long.valueOf(user.getUsername());
        UpdateTreatmentResponse response = treatmentService.modifyTreatment(memberId, UpdateTreatmentDto.of(treatmentId, treatmentRequest));

        return ApiResult.OK(response);
    }

    /**
     * 진료 정보 삭제 API
     * @param treatmentId 삭제할 진료 ID
     * @return 삭제한 진료 ID
     */
    @DeleteMapping("/{treatmentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<DeleteTreatmentResponse> deleteTreatment(@PathVariable Long treatmentId, @AuthenticationPrincipal User user) {
        log.info("TreatmentController#deleteTreatment called");

        Long memberId = Long.valueOf(user.getUsername());
        DeleteTreatmentResponse response = treatmentService.deleteTreatment(memberId, DeleteTreatmentDto.of(treatmentId));

        return ApiResult.OK(response);
    }
}

package com.wanchcoach.domain.treatment.controller;

import com.wanchcoach.domain.treatment.controller.request.CreatePrescriptionRequest;
import com.wanchcoach.domain.treatment.controller.request.CreateTreatmentRequest;
import com.wanchcoach.domain.treatment.controller.response.*;
import com.wanchcoach.domain.treatment.service.TreatmentQueryService;
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
    private final String urlPrefix = "https://objectstorage.ap-chuncheon-1.oraclecloud.com";
    private final TreatmentQueryService treatmentQueryService;

    /**
     * 진료 등록 API
     *
     * @param treatmentRequest 등록할 진료 정보
     * @return 등록된 진료 정보
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<CreateTreatmentResponse> createTreatment(@RequestPart CreateTreatmentRequest treatmentRequest,
                                                              @RequestPart MultipartFile file) throws Exception {

        log.info("TreatmentController#createTreatment called");
        // todo: 토큰 정보 가져와서 회원 ID 가져오기, 계정-가족 정보 유효한지 확인


        // 진료 정보 저장
        CreateTreatmentResponse treatmentResponse = treatmentService.createTreatment(CreateTreatmentDto.of(
                treatmentRequest.familyId(),
                treatmentRequest.hospitalId(),
                treatmentRequest.department(),
                treatmentRequest.date(),
                treatmentRequest.taken(),
                treatmentRequest.alarm(),
                treatmentRequest.symptom())
        );

        // 처방전(복약) 정보 저장
        CreatePrescriptionRequest prescriptionRequest = treatmentRequest.prescription();

        if (prescriptionRequest != null) {
            CreatePrescriptionResponse prescriptionResponse = treatmentService.createPrescription(CreatePrescriptionDto.of(
                    treatmentRequest.familyId(),
                    prescriptionRequest.pharmacyId(),
                    prescriptionRequest.morning(),
                    prescriptionRequest.noon(),
                    prescriptionRequest.evening(),
                    prescriptionRequest.beforeBed(),
                    prescriptionRequest.prescribedMedicines()), treatmentResponse.treatmentId(), file
            );

            treatmentResponse = new CreateTreatmentResponse(treatmentResponse.treatmentId(), prescriptionResponse.prescriptionId());
        }

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
                                                                    @RequestPart(required = false) MultipartFile file) throws Exception {
        log.info("TreatmentController#createPrescription called");

        // todo: todo: 토큰 정보 가져와서 회원 ID 가져오기, 계정-가족 정보 유효한지 확인


        CreatePrescriptionResponse response = treatmentService.createPrescription(CreatePrescriptionDto.of(
                prescriptionRequest.familyId(),
                prescriptionRequest.pharmacyId(),
                prescriptionRequest.morning(),
                prescriptionRequest.noon(),
                prescriptionRequest.evening(),
                prescriptionRequest.beforeBed(),
                prescriptionRequest.prescribedMedicines()), treatmentId, file
        );

        return ApiResult.OK(response);
    }

    /**
     * 전체 가족 진료 조회 API
     *
     * @return 전체 가족 진료 정보
     */
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentResponse> getTreatments() {
        log.info("TreatmentController#getTreatments called");

        // todo: 토큰 정보 가져와서 회원 ID 가져오기
        Long memberId = 1L;

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
    public ApiResult<TreatmentResponse> getFamilyTreatments(@PathVariable Long familyId) {
        log.info("TreatmentController#getTreatmentByFamilyId called");

        // todo: 토큰 정보 가져와서 회원 ID 가져오기, 계정-가족 정보 유효한지 확인
        Long memberId = 1L;

        TreatmentResponse response = treatmentQueryService.getFamilyTreatments(familyId);

        return ApiResult.OK(response);
    }

    /**
     * 전체 가족 진료 병원별 조회 API
     *
     * @return 전체 가족 병원별 진료 정보
     */
    @GetMapping("/hospitals")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResult<TreatmentHospitalResponse> getTreatmentsByHospital() {
        log.info("TreatmentController#getTreatmentsByHospital called");

        // todo: 토큰 정보 가져와서 회원 ID 가져오기
        Long memberId = 1L;

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
    public ApiResult<TreatmentHospitalResponse> getFamilyTreatmentsByHospital(@PathVariable Long familyId) {
        log.info("TreatmentController#getFamilyTreatmentsByHospital called");

        // todo: 토큰 정보 가져와서 회원 ID 가져오기, 계정-가족 정보 유효한지 확인
        Long memberId = 1L;

        TreatmentHospitalResponse response = treatmentQueryService.getFamilyTreatmentsByHospital(familyId);

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
    public ApiResult<TreatmentDateResponse> getTreatmentsByDate(@RequestParam Integer year, @RequestParam Integer month) {
        log.info("TreatmentController#getTreatmentsByDate called");

        // todo: 토큰 정보 가져와서 회원 ID 가져오기
        Long memberId = 1L;

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
    public ApiResult<TreatmentDateResponse> getFamilyTreatmentsByDate(@PathVariable Long familyId, @RequestParam Integer year, @RequestParam Integer month) {
        log.info("TreatmentController#getFamilyTreatmentsByDate called");

        // todo: 토큰 정보 가져와서 회원 ID 가져오기, 계정-가족 정보 유효한지 확인
        Long memberId = 1L;

        TreatmentDateResponse response = treatmentQueryService.getFamilyTreatmentsByDate(familyId, year, month);
        return ApiResult.OK(response);
    }

}

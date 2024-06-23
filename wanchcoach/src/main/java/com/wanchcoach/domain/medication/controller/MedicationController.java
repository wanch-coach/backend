package com.wanchcoach.domain.medication.controller;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.medication.service.MedicationQService;
import com.wanchcoach.domain.medication.service.MedicationService;
import com.wanchcoach.domain.treatment.service.TreatmentService;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static com.wanchcoach.global.api.ApiResult.ERROR;
import static com.wanchcoach.global.api.ApiResult.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medication")
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationQService medicationQService;
    private final TreatmentService treatmentService;

    //(홈) 오늘 약 정보 조회
    @GetMapping("/today")
    public ApiResult<?> getTodayInfo(){
        return OK(null);
    }

    //날짜 별(월,일) 가족 복약 조회
    @GetMapping("/")
    public ApiResult<?> getFamilyMedicationInfo(@RequestParam String year,@RequestParam String month,@RequestParam String day){
        return OK(null);
    }
    //복약 상세 조회
    @GetMapping("/prescriptions/{prescriptionId}")
    public ApiResult<?> getMedicationDetail(@PathVariable(value="prescriptionId") Long prescriptionId){
        try {
            List<SearchDrugsResponse> prescriptionDrugs = medicationQService.getMedicationDetail(prescriptionId);
            return OK(prescriptionDrugs);
        }catch(RuntimeException e){
            return ERROR(HttpStatus.NOT_FOUND,"등록된 처방전 혹은 약이 없습니다.");
        }
    }
    //복약 실행(약 먹기)
    @PatchMapping("/taken/{medicineRecordId}")
    public ApiResult<?> takenMedication(@PathVariable(value="medicineRecordId")Long medicineRecordId){
        return OK(null);
    }
    //복약 알림 여부 수정(처방전 On, Off)
    @PatchMapping("/alarm/{medicineRecordId}")
    public ApiResult<?> updateAlarm(@PathVariable(value="medicineRecordId")Long medicineRecordId){
        return OK(null);
    }
    //복약 삭제
    @DeleteMapping("/prescriptions/{prescriptionId}")
    public ApiResult<?> deletePrescription(@PathVariable(value="prescriptionId")Long prescriptionId){
        return OK(null);
    }
    //월별 가족 복약 조회(복약 이력/달력)
    @GetMapping("/families/{familyId}/year/{year}/month/{month}")
    public ApiResult<?> getMonthMedication(@PathVariable(value="familyId")Long familyId, @PathVariable(value="year")int year,@PathVariable(value="month")int month){
        return OK(null);
    }

    //일별 복약 상세 조회(복약 이력/달력)
    @GetMapping("/families/{familyId}")
    public ApiResult<?> getDayMedication(@PathVariable(value="familyId")Long familyId, @RequestParam String year,@RequestParam String month,@RequestParam String day){
        return OK(null);
    }

    //복약 이력 조회(처방전)
    @GetMapping("/records/families/{familyId} ")
    public ApiResult<?> getRecords(@PathVariable(value="familyId")Long familyId){
        return OK(null);
    }

    //내 약 정보 조회(지금까지 먹은 약)
    @GetMapping("/pills/families/{familyId}")
    public ApiResult<?> getPills(@PathVariable(value="familyId")Long familyId){
        return OK(null);
    }

    // 복약 종료
    @PatchMapping("/prescriptions/{prescriptionId}")
    public ApiResult<?> endPrescription(@PathVariable(value="prescriptionId")Long prescriptionId){
        try{
            treatmentService.endPrescription(prescriptionId);
            return OK(null);
        }catch(NoSuchElementException e){
            return ERROR(HttpStatus.NO_CONTENT, "해당 처방전이 존재하지 않습니다.");
        }
    }

}

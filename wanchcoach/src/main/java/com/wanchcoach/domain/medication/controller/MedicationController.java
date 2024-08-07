package com.wanchcoach.domain.medication.controller;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.medication.controller.response.*;
import com.wanchcoach.domain.medication.controller.request.TakingMedicineRequest;
import com.wanchcoach.domain.medication.service.MedicationQService;
import com.wanchcoach.domain.medication.service.MedicationService;
import com.wanchcoach.domain.medication.service.dto.GetPillsDto;
import com.wanchcoach.domain.medication.service.dto.TakingMedicineDto;
import com.wanchcoach.domain.treatment.service.TreatmentService;
import com.wanchcoach.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public ApiResult<TodayMedicationResponse> getTodayInfo(@AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        TodayMedicationResponse todayMedicationResponse = medicationQService.getTodayMedication(memberId);
        return OK(todayMedicationResponse);
    }

    //날짜 별(월,일) 가족 복약 조회
    @GetMapping
    public ApiResult<?> getFamilyMedicationInfo(@RequestParam int year,@RequestParam int month,@RequestParam int day, @AuthenticationPrincipal User user){

        Long memberId = Long.valueOf(user.getUsername());
        List<DailyPrescriptionResponse> response = medicationQService.getDailyPrescription(year,month,day, memberId);

        return OK(response);
    }
    //복약 상세 조회
    @GetMapping("/prescriptions/{prescriptionId}")
    public ApiResult<?> getMedicationDetail(@PathVariable(value="prescriptionId") Long prescriptionId){
        try {
            List<SearchDrugsResponse> prescriptionDrugs = medicationQService.getMedicationDetail(prescriptionId);
            return OK(prescriptionDrugs);
        }catch(RuntimeException e){
            return ERROR(HttpStatus.NO_CONTENT,"등록된 처방전 혹은 약이 없습니다.");
        }
    }
    //복약 실행(약 먹기)
    @PostMapping("/taken/{prescriptionId}")
    public ApiResult<?> takenMedication(@PathVariable(value="prescriptionId")Long prescriptionId, @RequestBody TakingMedicineRequest takingMedicineRequest){
        try{
            medicationService.takenMedicine(TakingMedicineDto.of(prescriptionId, takingMedicineRequest));
            return OK(null);
        }catch(RuntimeException e){
            return ERROR(HttpStatus.NO_CONTENT, "복용중이지 않거나 잘못된 처방전 정보입니다.");
        }
    }
    //복약 알림 여부 수정(처방전 On, Off)
    @PatchMapping("/alarm/{prescriptionId}")
    public ApiResult<?> updateAlarm(@PathVariable(value="prescriptionId")Long prescriptionId){
        Long treatmentId = medicationQService.getTreatmentIdByPrescriptionId(prescriptionId);
        medicationService.changeAlarm(treatmentId);
        return OK(prescriptionId);
    }

    //월별 복약 이력 조회(복약 이력/달력)
    @GetMapping("/families/{familyId}/year/{year}/month/{month}")
    public ApiResult<?> getMonthMedication(@PathVariable(value="familyId")Long familyId, @PathVariable(value="year")int year,@PathVariable(value="month")int month){

        if(year> LocalDateTime.now().getYear() || !(1<=month &&month<=12)){
            return ERROR(HttpStatus.BAD_REQUEST, "요청 날짜의 정보가 잘못되었습니다.");
        };
        RecordCalendarResponse recordCalendarResponse = medicationQService.getCalendarRecord(familyId, year, month);

        return OK(recordCalendarResponse);
    }

    //복약 이력 조회(처방전)
    @GetMapping("/records/families/{familyId}")
    public ApiResult<?> getRecords(@PathVariable(name="familyId") Long familyId){
        try {
            PrescriptionRecordResponse prescriptionRecordResponse = medicationQService.getPrescriptionRecord(familyId);
            return OK(prescriptionRecordResponse);
        }catch(RuntimeException e){
            return ERROR(HttpStatus.NOT_FOUND, "잘못된 가족 정보입니다.");
        }
    }

    //내 약 정보 조회(지금까지 먹은 약)
    @GetMapping("/pills/families/{familyId}")
    public ApiResult<?> getPills(@PathVariable(value="familyId")Long familyId, @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date")LocalDate endDate){
        List<TakenPillsResponse> takenPillsWithRecord =  medicationQService.getPills(GetPillsDto.of(familyId,startDate, endDate));
        return OK(takenPillsWithRecord);
    }

    // 복약 종료
    @PatchMapping("/prescriptions/{prescriptionId}")
    public ApiResult<?> endPrescription(@PathVariable(value="prescriptionId")Long prescriptionId, @AuthenticationPrincipal User user){
        try{
            Long memberId = Long.valueOf(user.getUsername());
            treatmentService.endPrescription(memberId, prescriptionId);
            return OK(null);
        }catch(NoSuchElementException e){
            return ERROR(HttpStatus.NO_CONTENT, "해당 처방전이 존재하지 않습니다.");
        }
    }

}

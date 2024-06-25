package com.wanchcoach.domain.medication.service;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import com.wanchcoach.domain.medication.controller.response.*;
import com.wanchcoach.domain.medication.repository.MedicationQRepository;
import com.wanchcoach.domain.medication.service.dto.CalendarRecordDto;
import com.wanchcoach.domain.medication.service.dto.PrescriptionListDto;
import com.wanchcoach.domain.medication.service.dto.PrescriptionRecordDto;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MedicationQService {

    private final MedicationQRepository medicationQRepository;

    public List<SearchDrugsResponse> getMedicationDetail(Long prescriptionId){
        List<SearchDrugsDto> drugInfo = medicationQRepository.findPrescriptionsDrugs(prescriptionId);
        List<SearchDrugsResponse> drugList = new ArrayList<>();

        if (drugInfo == null) {
            throw new NotFoundException(Prescription.class, prescriptionId);
        }

        for(SearchDrugsDto searchDrugsDto:drugInfo){
            drugList.add(searchDrugsDto.toSearchDrugsResponse());
        }
        return drugList;
    }

    public TodayMedicationResponse getTodayMedication(Long memberId){
        TodayMedicationResponse todayMedicationResponse = medicationQRepository.getTodayMedications(memberId);
        return todayMedicationResponse;
    }

    public PrescriptionRecordResponse getPrescriptionRecord(Long familyId){
        List<PrescriptionRecordDto> prescriptionListDto = medicationQRepository.getPrescriptionRecord(familyId);

        List<PrescriptionTakingRecord> prescriptionTaking = new ArrayList<>();
        List<PrescriptionEndRecord> prescriptionEnd = new ArrayList<>();

        for(PrescriptionRecordDto val : prescriptionListDto){

            if(val.taking()){
                prescriptionTaking.add(val.toPrescriptionTakingRecord());
            }else{
                prescriptionEnd.add(val.toPrescriptionEndRecord());
            }
        }

        return new PrescriptionRecordResponse(prescriptionTaking, prescriptionEnd);
    }

    public RecordCalendarResponse getCalendarRecord(Long familyId, int year, int month){

        //모든 복약 기록
        List<CalendarRecordDto> records = medicationQRepository.getCalendarRecord(familyId, year, month);

        // 반환 값
        List<RecordCalendarDay> dayRecord = new ArrayList<>();

        //그 달의 모든 복약 기록
        for(CalendarRecordDto record : records){

            boolean insert = false;

            for(RecordCalendarDay dayInfo : dayRecord){
                //이미 일자 정보가 있는 경우
                if(dayInfo.day()==record.prescription().takenDay().getDayOfMonth()){
                    //복약 시기에 따라 변환 후 추가
                    if(record.prescription().time().equals("morning")){
                        dayInfo.morning().add(record.toRecordCalendarDayInfo());
                    }else if(record.prescription().time().equals("noon")){
                        dayInfo.noon().add(record.toRecordCalendarDayInfo());
                    }else if(record.prescription().time().equals("evening")){
                        dayInfo.evening().add(record.toRecordCalendarDayInfo());
                    }else{
                        dayInfo.beforeBed().add(record.toRecordCalendarDayInfo());
                    }
                    insert = true;
                    break;
                }
            }
            if(!insert){

                RecordCalendarDay rcd = new RecordCalendarDay(record.prescription().takenDay().getDayOfMonth(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                if(record.prescription().time().equals("morning")){
                    rcd.morning().add(record.toRecordCalendarDayInfo());
                }else if(record.prescription().time().equals("noon")){
                    rcd.noon().add(record.toRecordCalendarDayInfo());
                }else if(record.prescription().time().equals("evening")){
                    rcd.evening().add(record.toRecordCalendarDayInfo());
                }else{
                    rcd.beforeBed().add(record.toRecordCalendarDayInfo());
                }
                dayRecord.add(rcd);
            }
        }

        return new RecordCalendarResponse(year, month, dayRecord);
    }
}

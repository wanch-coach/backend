package com.wanchcoach.domain.medication.service;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import com.wanchcoach.domain.medication.controller.response.PrescriptionEndRecord;
import com.wanchcoach.domain.medication.controller.response.PrescriptionRecordResponse;
import com.wanchcoach.domain.medication.controller.response.PrescriptionTakingRecord;
import com.wanchcoach.domain.medication.controller.response.TodayMedicationResponse;
import com.wanchcoach.domain.medication.repository.MedicationQRepository;
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
}

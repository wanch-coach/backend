package com.wanchcoach.domain.medication.service.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import com.wanchcoach.domain.medication.controller.response.RecordCalendarDayInfo;

import java.util.ArrayList;
import java.util.List;

public record CalendarRecordDto(
        MedicineRecordPrescriptionDto prescription,
        List<SearchDrugsDto> drugList
) {

    public RecordCalendarDayInfo toRecordCalendarDayInfo(){


        List<SearchDrugsResponse> drugs = new ArrayList<>();

        for(SearchDrugsDto dto : drugList){
            drugs.add(dto.toSearchDrugsResponse());
        }

        return new RecordCalendarDayInfo(prescription.hospitalName(), prescription.department(), prescription.prescriptionId(), drugs);


    }
}

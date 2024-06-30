package com.wanchcoach.domain.medication.service.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import com.wanchcoach.domain.medication.controller.response.PrescriptionEndRecord;
import com.wanchcoach.domain.medication.controller.response.PrescriptionTakingRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PrescriptionRecordDto (
    Long hospitalId,
    String hospitalName,
    String department,
    LocalDateTime start,
    LocalDate end,
    Long prescriptionId,
    Boolean taking,
    List<SearchDrugsDto> drugs
){

    public PrescriptionTakingRecord toPrescriptionTakingRecord(){

        List<SearchDrugsResponse> drugResponse = new ArrayList<>();

        for(SearchDrugsDto dto : drugs){
            drugResponse.add(dto.toSearchDrugsResponse());
        }

        return new PrescriptionTakingRecord(
                this.hospitalId,
                this.hospitalName,
                this.department,
                this.start,
                this.prescriptionId,
                drugResponse
        );
    }
    public PrescriptionEndRecord toPrescriptionEndRecord(){

        List<SearchDrugsResponse> drugResponse = new ArrayList<>();

        for(SearchDrugsDto dto : drugs){
            drugResponse.add(dto.toSearchDrugsResponse());
        }

        return new PrescriptionEndRecord(
                this.hospitalId,
                this.hospitalName,
                this.department,
                this.start,
                this.end,
                this.prescriptionId,
                drugResponse
        );
    }
}

package com.wanchcoach.domain.medication.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;

import com.wanchcoach.domain.medication.service.PrescriptionRecordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanchcoach.domain.medical.entity.QHospital.hospital;
import static com.wanchcoach.domain.treatment.entity.QPrescribedDrug.prescribedDrug;
import static com.wanchcoach.domain.treatment.entity.QPrescription.prescription;
import static com.wanchcoach.domain.drug.entity.QDrug.drug;
import static com.wanchcoach.domain.drug.entity.QDrugImage.drugImage;
import static com.wanchcoach.domain.treatment.entity.QTreatment.treatment;

@Repository
@RequiredArgsConstructor
public class MedicationQRepository {

    private final JPAQueryFactory queryFactory;

    public List<SearchDrugsDto> findPrescriptionsDrugs(Long prescriptionId){

        List<SearchDrugsDto> drugList  = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                        drug.drugId,
                        drug.itemName,
                        drug.spcltyPblc,
                        drugImage.filePath.coalesce("")
                ))
                .from(prescription)
                .join(prescribedDrug).on(prescription.prescriptionId.eq(prescribedDrug.prescription.prescriptionId))
                .join(drug).on(prescribedDrug.drug.drugId.eq(drug.drugId))
                .join(drugImage).on(drug.drugImage.drugImageId.eq(drugImage.drugImageId))
                .where(prescription.prescriptionId.eq(prescriptionId))
                .fetch();
        return drugList;
    }

    public List<PrescriptionRecordDto> xxx(Long familyId){
        List<PrescriptionRecordDto> prescriptionRecordList = queryFactory.select(Projections.constructor(PrescriptionRecordDto.class,
                    hospital.hospitalId,
                hospital.name,
                treatment.department,
                prescription.createdDate,
                prescription.endDate,
                prescription.prescriptionId
                ))
                .from(treatment)
                .join(hospital).on(treatment.hospital.hospitalId.eq(hospital.hospitalId))
                .join(prescription).on(prescription.prescriptionId.eq(treatment.prescription.prescriptionId))
                .where(treatment.family.familyId.eq(familyId))
                .fetch();
        return prescriptionRecordList;
    }

}

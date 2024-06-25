package com.wanchcoach.domain.medication.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;

import com.wanchcoach.domain.medication.controller.response.TakenPillsResponse;
import com.wanchcoach.domain.medication.service.dto.*;
import com.wanchcoach.domain.medication.controller.response.TodayMedicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.wanchcoach.domain.medical.entity.QHospital.hospital;
import static com.wanchcoach.domain.family.entity.QFamily.family;
import static com.wanchcoach.domain.medication.entity.QMedicineRecord.medicineRecord;
import static com.wanchcoach.domain.member.entity.QMember.member;
import static com.wanchcoach.domain.treatment.entity.QTreatment.treatment;
import static com.wanchcoach.domain.treatment.entity.QPrescribedDrug.prescribedDrug;
import static com.wanchcoach.domain.treatment.entity.QPrescription.prescription;
import static com.wanchcoach.domain.drug.entity.QDrug.drug;
import static com.wanchcoach.domain.drug.entity.QDrugImage.drugImage;

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

    public TodayMedicationResponse getTodayMedications(Long memberId){


        List<TodayMedicationDto> today = queryFactory.select(Projections.constructor(TodayMedicationDto.class,
                        family.familyId,
                        family.name,
                        hospital.name,
                        treatment.department,
                        prescription.morning,
                        prescription.noon,
                        prescription.evening,
                        prescription.beforeBed
                        ))
                .from(member)
                .join(family).on(member.memberId.eq(family.member.memberId))
                .join(treatment).on(family.familyId.eq(treatment.family.familyId))
                .join(prescription).on(treatment.prescription.prescriptionId.eq(prescription.prescriptionId))
                .join(hospital).on(treatment.hospital.hospitalId.eq(hospital.hospitalId))
                .where(member.memberId.eq(memberId).and(prescription.taking.eq(true)))
                .fetch();

        List<TodayMedicationDto> morning = new ArrayList<>();
        List<TodayMedicationDto> noon = new ArrayList<>();
        List<TodayMedicationDto> evening = new ArrayList<>();
        List<TodayMedicationDto> beforeBed = new ArrayList<>();

        for(TodayMedicationDto val : today){
            if(val.getMorning()){
                morning.add(val);
            }
            if(val.getNoon()){
                noon.add(val);
            }
            if(val.getEvening()){
                evening.add(val);
            }
            if(val.getBeforeBed()){
                beforeBed.add(val);
            }
        }
        TodayMedicationResponse todayMedicationResponse = new TodayMedicationResponse(morning, noon, evening, beforeBed);
        return todayMedicationResponse;
    }


    public List<PrescriptionRecordDto> getPrescriptionRecord(Long familyId){

        List<PrescriptionRecordDto> prescriptions = new ArrayList<>();

        List<PrescriptionListDto> prescriptionList = queryFactory.select(Projections.constructor(PrescriptionListDto.class,
                        hospital.hospitalId,
                        hospital.name,
                        treatment.department,
                        prescription.createdDate,
                        prescription.endDate,
                        prescription.prescriptionId,
                        prescription.taking
                ))
                .from(treatment)
                .join(family).on(treatment.family.familyId.eq(family.familyId))
                .join(hospital).on(treatment.hospital.hospitalId.eq(hospital.hospitalId))
                .join(prescription).on(prescription.prescriptionId.eq(treatment.prescription.prescriptionId))
                .where(family.familyId.eq(familyId))
                .fetch();

        for(PrescriptionListDto prl : prescriptionList){
            List<SearchDrugsDto> drugList  = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                            drug.drugId,
                            drug.itemName,
                            drug.spcltyPblc,
                            drugImage.filePath.coalesce("")
                    ))
                    .from(prescribedDrug)
                    .join(drug).on(drug.drugId.eq(prescribedDrug.drug.drugId))
                    .leftJoin(drugImage).on(drug.drugImage.drugImageId.eq(drugImage.drugImageId))
                    .where(prescribedDrug.prescription.prescriptionId.eq(prl.prescriptionId()))
                    .fetch();

            prescriptions.add(new PrescriptionRecordDto(prl.hospitalId(), prl.hospitalName(), prl.department(), prl.start(), prl.end(), prl.prescriptionId(), prl.taking(), drugList));

        }

        return prescriptions;
    }

    public List<CalendarRecordDto> getCalendarRecord(Long familyId, int year, int month){

        List<CalendarRecordDto> recordWithDrugs = new ArrayList<>();

        List<MedicineRecordPrescriptionDto> prescriptionList = queryFactory.select(Projections.constructor(MedicineRecordPrescriptionDto.class,
                        hospital.hospitalId,
                        hospital.name,
                        treatment.department,
                        prescription.prescriptionId,
                        prescription.createdDate,
                        medicineRecord.time
                ))
                .from(treatment)
                .join(family).on(treatment.family.familyId.eq(family.familyId))
                .join(hospital).on(treatment.hospital.hospitalId.eq(hospital.hospitalId))
                .join(prescription).on(prescription.prescriptionId.eq(treatment.prescription.prescriptionId))
                .join(medicineRecord).on(medicineRecord.prescription.prescriptionId.eq(prescription.prescriptionId))
                .where(family.familyId.eq(familyId).and(medicineRecord.createdDate.year().eq(year).and(medicineRecord.createdDate.month().eq(month))))
                .fetch();

        for(MedicineRecordPrescriptionDto dto : prescriptionList) {
            List<SearchDrugsDto> drugList = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                            drug.drugId,
                            drug.itemName,
                            drug.spcltyPblc,
                            drugImage.filePath.coalesce("")
                    ))
                    .from(prescribedDrug)
                    .join(prescription).on(prescription.prescriptionId.eq(prescribedDrug.prescription.prescriptionId))
                    .join(drug).on(drug.drugId.eq(prescribedDrug.drug.drugId))
                    .leftJoin(drugImage).on(drug.drugImage.drugImageId.eq(drugImage.drugImageId))
                    .where(prescription.prescriptionId.eq(dto.prescriptionId()))
                    .fetch();
            recordWithDrugs.add(new CalendarRecordDto(dto,drugList));
        }
        return recordWithDrugs;
    }

    public List<TakenPillsResponse> getTakenPills(GetPillsDto dto){

        List<TakenPillsResponse> pillsDto = new ArrayList<>();

        List<SearchDrugsDto> drugList = queryFactory.select(Projections.constructor(SearchDrugsDto.class,
                        drug.drugId,
                        drug.itemName,
                        drug.spcltyPblc,
                        drugImage.filePath.coalesce("")
                ))
                .from(medicineRecord)
                .join(family).on(medicineRecord.family.familyId.eq(family.familyId))
                .join(prescription).on(prescription.prescriptionId.eq(medicineRecord.prescription.prescriptionId))
                .join(prescribedDrug).on(prescribedDrug.prescription.prescriptionId.eq(prescription.prescriptionId))
                .join(drug).on(prescribedDrug.drug.drugId.eq(drug.drugId))
                .leftJoin(drugImage).on(drug.drugImage.drugImageId.eq(drugImage.drugImageId))
                .where(family.familyId.eq(dto.familyId()).and(medicineRecord.createdDate.between(dto.startDate().atStartOfDay(), dto.endDate().plusDays(1).atStartOfDay())))
                .groupBy(drug.drugId)
                .fetch();

        for(SearchDrugsDto drugDto : drugList){
            List<SearchMedicineRecordDto> medicineRecordList = queryFactory.select(Projections.constructor(SearchMedicineRecordDto.class,
                    medicineRecord.createdDate
                    ))
                    .from(medicineRecord)
                    .join(prescription).on(prescription.prescriptionId.eq(medicineRecord.prescription.prescriptionId))
                    .join(prescribedDrug).on(prescribedDrug.prescription.prescriptionId.eq(prescription.prescriptionId))
                    .join(drug).on(drug.drugId.eq(prescribedDrug.drug.drugId))
                    .where(drug.drugId.eq(drugDto.getDrugId()).and(medicineRecord.createdDate.between(dto.startDate().atStartOfDay(), dto.endDate().plusDays(1).atStartOfDay())))
                    .fetch();
            pillsDto.add(new TakenPillsResponse(drugDto.toSearchDrugsResponse(), medicineRecordList));
        }
        return pillsDto;
    }
}

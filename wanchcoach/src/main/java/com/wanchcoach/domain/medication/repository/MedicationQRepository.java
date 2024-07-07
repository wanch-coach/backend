package com.wanchcoach.domain.medication.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.medication.controller.response.*;
import com.wanchcoach.domain.medication.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wanchcoach.domain.drug.entity.QFavoriteDrug.favoriteDrug;
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
                        drug.prductType,
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
                            drug.prductType,
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
                            drug.prductType,
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
                        drug.prductType,
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

    public DailyPrescriptionResponse getDailyPrescriptions(int year, int month, int day, Long familyId){

        List<DailyPrescriptionInfo> morningTaken = new ArrayList<>();
        List<DailyPrescriptionInfo> morningUnTaken= new ArrayList<>();

        List<DailyPrescriptionInfo> noonTaken= new ArrayList<>();
        List<DailyPrescriptionInfo> noonUnTaken= new ArrayList<>();

        List<DailyPrescriptionInfo> eveningTaken= new ArrayList<>();
        List<DailyPrescriptionInfo> eveningUnTaken= new ArrayList<>();

        List<DailyPrescriptionInfo> beforeBedTaken= new ArrayList<>();
        List<DailyPrescriptionInfo> beforeBedUnTaken= new ArrayList<>();


        //가족의 복용중인 처방전 목록
        List<DailyPrescriptionDto> prescriptionList = queryFactory.select(Projections.constructor(DailyPrescriptionDto.class,
                        prescription.remains,
                        hospital.name,
                        treatment.department,
                        prescription.prescriptionId,
                        prescription.morning,
                        prescription.noon,
                        prescription.evening,
                        prescription.beforeBed
                        ))
                .from(family)
                .join(treatment).on(treatment.family.familyId.eq(family.familyId))
                .join(hospital).on(treatment.hospital.hospitalId.eq(hospital.hospitalId))
                .join(prescription).on(prescription.prescriptionId.eq(treatment.prescription.prescriptionId))
                .where(family.familyId.eq(familyId).and(prescription.taking.eq(true)))
                .fetch();

        //가족의 오늘 복약 내역
        List<MedicineRecordDto> recordList = queryFactory.select(Projections.constructor(MedicineRecordDto.class,
                        prescription.prescriptionId,
                        medicineRecord.time
                ))
                .from(family)
                .join(medicineRecord).on(medicineRecord.family.familyId.eq(family.familyId))
                .join(prescription).on(medicineRecord.prescription.prescriptionId.eq(prescription.prescriptionId))
                .where(family.familyId.eq(familyId).and(
                        medicineRecord.createdDate.year().eq(year).and(
                                medicineRecord.createdDate.month().eq(month).and(
                                        medicineRecord.createdDate.dayOfMonth().eq(day)))))
                .fetch();

        //처방전 목록 순회
        for(DailyPrescriptionDto pst: prescriptionList){

            //처방전 약 목록 조회
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
                    .where(prescription.prescriptionId.eq(pst.prescriptionId()))
                    .fetch();

            //약 이미지 파일 조회
            List<SearchDrugsResponse> drugResponse = new ArrayList<>();
            for(SearchDrugsDto dto: drugList){
                drugResponse.add(dto.toSearchDrugsResponse());
            }

            if(pst.morning()){
                morningUnTaken.add(new DailyPrescriptionInfo(pst.prescriptionId(), pst.hospitalName(), pst.department(), pst.remains(), drugResponse));
            }
            if(pst.noon()){
                noonUnTaken.add(new DailyPrescriptionInfo(pst.prescriptionId(), pst.hospitalName(), pst.department(), pst.remains(), drugResponse));
            }
            if(pst.evening()){
                eveningUnTaken.add(new DailyPrescriptionInfo(pst.prescriptionId(), pst.hospitalName(), pst.department(), pst.remains(), drugResponse));
            }
            if(pst.beforeBed()){
                beforeBedUnTaken.add(new DailyPrescriptionInfo(pst.prescriptionId(), pst.hospitalName(), pst.department(), pst.remains(), drugResponse));
            }

            //복용 기록
            for(MedicineRecordDto record : recordList){
                //복용했다면
                if(record.prescriptionId().equals(pst.prescriptionId())){
                    if(record.time()==0){
                        for(DailyPrescriptionInfo info : morningUnTaken){
                            if(info.prescriptionId().equals(record.prescriptionId())){
                                morningTaken.add(info);
                                morningUnTaken.remove(info);
                            }
                        }
                    }else if(record.time()==1){
                        for(DailyPrescriptionInfo info : noonUnTaken){
                            if(info.prescriptionId().equals(record.prescriptionId())){
                                noonTaken.add(info);
                                noonUnTaken.remove(info);
                            }
                        }
                    }else if(record.time()==2){
                        for(DailyPrescriptionInfo info : eveningUnTaken){
                            if(info.prescriptionId().equals(record.prescriptionId())){
                                eveningTaken.add(info);
                                eveningUnTaken.remove(info);
                            }
                        }
                    }else{
                        for(DailyPrescriptionInfo info : beforeBedUnTaken){
                            if(info.prescriptionId().equals(record.prescriptionId())){
                                beforeBedTaken.add(info);
                                beforeBedUnTaken.remove(info);
                            }
                        }
                    }
                }
            }
        }
        DailyPrescription morning = new DailyPrescription(morningUnTaken, morningTaken);
        DailyPrescription noon = new DailyPrescription(noonUnTaken, noonTaken);
        DailyPrescription evening = new DailyPrescription(eveningUnTaken, eveningTaken);
        DailyPrescription beforeBed = new DailyPrescription(beforeBedUnTaken, beforeBedTaken);
        return new DailyPrescriptionResponse(familyId, morning, noon, evening, beforeBed);
    }
}

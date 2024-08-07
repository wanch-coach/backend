package com.wanchcoach.domain.medication.service;

import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.repository.command.FamilyRepository;
import com.wanchcoach.domain.medication.controller.request.TakingMedicineRequest;
import com.wanchcoach.domain.medication.controller.response.AlarmChangeResponse;
import com.wanchcoach.domain.medication.entity.MedicineRecord;
import com.wanchcoach.domain.medication.repository.MedicineRecordRepository;
import com.wanchcoach.domain.medication.service.dto.TakingMedicineDto;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.domain.treatment.entity.Treatment;
import com.wanchcoach.domain.treatment.repository.command.PrescriptionRepository;
import com.wanchcoach.domain.treatment.repository.command.TreatmentRepository;
import com.wanchcoach.domain.treatment.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicationService {

    private final TreatmentRepository treatmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRecordRepository medicineRecordRepository;
    private final FamilyRepository familyRepository;


    public void takenMedicine(TakingMedicineDto takingMedicineDto){

        Family family = familyRepository.findByFamilyId(takingMedicineDto.familyId()).orElseThrow();
        Prescription prescription = prescriptionRepository.findByPrescriptionId(takingMedicineDto.prescriptionId()).orElseThrow();
        if(prescription.getRemains()>0){
            prescription.takenMedicine();
            MedicineRecord medicineRecord = MedicineRecord.builder()
                    .family(family)
                    .prescription(prescription)
                    .time(takingMedicineDto.time())
                    .takenDate(takingMedicineDto.takenDate())
                    .build();
            medicineRecordRepository.save(medicineRecord);
        }else{
            throw new RuntimeException();
        }
    }

    public void changeAlarm(Long treatmentId){
        Treatment treatment = treatmentRepository.findById(treatmentId).orElseThrow();
        treatment.updateAlarm();
    }
}

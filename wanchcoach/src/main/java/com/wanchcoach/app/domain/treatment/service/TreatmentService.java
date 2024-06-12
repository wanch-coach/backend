package com.wanchcoach.app.domain.treatment.service;

import com.wanchcoach.app.domain.treatment.controller.response.CreatePrescriptionResponse;
import com.wanchcoach.app.domain.treatment.controller.response.CreateTreatmentResponse;
import com.wanchcoach.app.domain.treatment.entity.PrescribedMedicine;
import com.wanchcoach.app.domain.treatment.entity.Prescription;
import com.wanchcoach.app.domain.treatment.entity.Treatment;
import com.wanchcoach.app.domain.treatment.repository.command.PrescribedMedicineRepository;
import com.wanchcoach.app.domain.treatment.repository.command.PrescriptionRepository;
import com.wanchcoach.app.domain.treatment.repository.command.TreatmentRepository;
import com.wanchcoach.app.domain.treatment.repository.query.PrescriptionQueryRepository;
import com.wanchcoach.app.domain.treatment.repository.query.TreatmentQueryRepository;
import com.wanchcoach.app.domain.treatment.service.dto.CreatePrescribedMedicineDto;
import com.wanchcoach.app.domain.treatment.service.dto.CreatePrescriptionDto;
import com.wanchcoach.app.domain.treatment.service.dto.CreateTreatmentDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 진료 서비스
 *
 * @author 박은규
 *
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatmentQueryRepository treatmentQueryRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescribedMedicineRepository prescribedMedicineRepository;
    private final PrescriptionQueryRepository prescriptionQueryRepository;

    /**
     * 진료 등록 API
     *
     * @param dto 등록할 진료 정보
     *
     */
    public CreateTreatmentResponse createTreatment(CreateTreatmentDto dto) {
        // todo: 계정 정보 - 가족 정보 연결 확인
        
        // 저장
        Treatment savedTreatment = treatmentRepository.save(Treatment.builder()
                // todo: .family(FamilyQueryRepository.findById(dto.familyId())
                .hospital(null) // todo: .hospital(.findById(dto.hospitalId())
                .prescription(prescriptionQueryRepository.findById(dto.prescriptionId()))
                .department(dto.department())
                .date(LocalDateTime.parse(dto.date()))
                .taken(dto.taken())
                .alarm(dto.alarm())
                .symptom(dto.symptom())
                .build()
        );

        // 진료 id 반환
        return new CreateTreatmentResponse(savedTreatment.getId());
    }

    /**
     * 처방전 등록 API
     *
     * @param dto 등록할 처방전(복용할 약) 정보
     */
    public CreatePrescriptionResponse createPrescription(CreatePrescriptionDto dto, Long treatmentId, MultipartFile file) {
        // todo: 계정 정보 - 가족 정보 연결 확인

        // todo: 처방전 이미지 저장

        // 남은 복약 횟수 계산
        int maxRemains = 0;
        for (CreatePrescribedMedicineDto cpmDto: dto.prescribedMedicines()) {
            maxRemains = Math.max(maxRemains, cpmDto.frequency() * cpmDto.day());
        }

        // 처방전 저장
        Prescription prescription = prescriptionRepository.save(Prescription.builder()
                .pharmacy(null)         // todo: .pharmacy(pharmacyQueryRepository.findById(dto.pharmacyId())
                .remains(maxRemains)    // todo: 전체 복약횟수 계산 로직 개선
                .taking(true)
                .endDate(LocalDate.now())
                .build()
        );

        // 처방받은 약 저장
        for (CreatePrescribedMedicineDto cpmDto : dto.prescribedMedicines()) {
            prescribedMedicineRepository.save(PrescribedMedicine.builder()
                    .prescription(prescription)
                    .quantity(cpmDto.Quantity())
                    .frequency(cpmDto.frequency())
                    .day(cpmDto.day())
                    .direction(cpmDto.direction())
                    .build()
            );
        }

        // 진료 정보에 저장
        Treatment treatment = treatmentQueryRepository.findById(treatmentId);
        treatment.updatePrescription(prescription);

        // todo: 예정된 복약 기록 저장
        /*
            medicalService.createScheduledMedicineRecord();
            1. 진료 ID로 가족 찾기
                Long familyId = treatment.getFamily().getId();
            2. 진료 시간 체크
                LocalDateTime date = treatment.getDate();
            3. 가족 복약 시간 체크
            4. 진료 시간 기준 다음 복약 시간부터 예정된 복약에 등록
            5. 알림 여부는 dto.morning, dto.noon, dto.evening, dto.beforeBed 값으로 가져오기
         */

        // 처방전 id 반환
        return new CreatePrescriptionResponse(prescription.getId());
    }
}

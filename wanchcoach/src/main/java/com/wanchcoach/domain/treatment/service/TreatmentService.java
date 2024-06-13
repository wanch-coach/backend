package com.wanchcoach.domain.treatment.service;

import com.wanchcoach.domain.family.repository.query.FamilyQueryRepository;
import com.wanchcoach.domain.medical.repository.query.HospitalQueryRepository;
import com.wanchcoach.domain.medical.repository.query.PharmacyQueryRepository;
import com.wanchcoach.domain.drug.repository.query.DrugQueryRepository;
import com.wanchcoach.domain.treatment.controller.response.CreatePrescriptionResponse;
import com.wanchcoach.domain.treatment.controller.response.CreateTreatmentResponse;
import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.domain.treatment.entity.Treatment;
import com.wanchcoach.domain.treatment.repository.command.PrescribedDrugRepository;
import com.wanchcoach.domain.treatment.repository.command.PrescriptionRepository;
import com.wanchcoach.domain.treatment.repository.command.TreatmentRepository;
import com.wanchcoach.domain.treatment.repository.query.TreatmentQueryRepository;
import com.wanchcoach.domain.treatment.service.dto.CreatePrescribedMedicineDto;
import com.wanchcoach.domain.treatment.service.dto.CreatePrescriptionDto;
import com.wanchcoach.domain.treatment.service.dto.CreateTreatmentDto;
import com.wanchcoach.global.service.UploadFileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private final PrescriptionRepository prescriptionRepository;
    private final PrescribedDrugRepository prescribedDrugRepository;
    private final HospitalQueryRepository hospitalQueryRepository;
    private final PharmacyQueryRepository pharmacyQueryRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final DrugQueryRepository drugQueryRepository;

    private final UploadFileService uploadFileService;
    private final TreatmentQueryRepository treatmentQueryRepository;

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
                .family(familyQueryRepository.findById(dto.familyId()))
                .hospital(hospitalQueryRepository.findById(dto.hospitalId()))
                .prescription(null)
                .department(dto.department())
                .date(LocalDateTime.parse(dto.date()))
                .taken(dto.taken())
                .alarm(dto.alarm())
                .symptom(dto.symptom())
                .build()
        );

        // 진료 id 반환
        return new CreateTreatmentResponse(savedTreatment.getId(), null);
    }

    /**
     * 처방전 등록 API
     *
     * @param dto 등록할 처방전(복용할 약) 정보
     */
    public CreatePrescriptionResponse createPrescription(CreatePrescriptionDto dto, Long treatmentId, MultipartFile file) throws Exception {

        // 남은 복약 횟수 계산
        int maxRemains = 0;
        for (CreatePrescribedMedicineDto cpmDto: dto.prescribedMedicines()) {
            maxRemains = Math.max(maxRemains, cpmDto.frequency() * cpmDto.day());
        }

        // 처방전 저장
        Prescription prescription = prescriptionRepository.save(Prescription.builder()
                .pharmacy(pharmacyQueryRepository.findById(dto.pharmacyId()))
                .remains(maxRemains)    // todo: 전체 복약횟수 계산 로직 개선
                .taking(true)
                .endDate(null)
                .build()
        );

        // 진료 처방전 정보 저장
        Treatment treatment = treatmentQueryRepository.findById(treatmentId);
        treatment.updatePrescription(prescription);

        // 처방전 이미지 저장
        String fileName = familyQueryRepository.findNameById(dto.familyId()) +
                "_" +
                prescription.getId() +
                "_" +
                prescription.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                "." +
                StringUtils.getFilenameExtension(file.getOriginalFilename());

        prescription.updateImageFileName(fileName);
        fileName = "prescription/" + dto.familyId() + "/" + fileName;
        uploadFileService.uploadPrescriptionImage(file, fileName);

        // 처방받은 약 저장
        for (CreatePrescribedMedicineDto cpmDto : dto.prescribedMedicines()) {
            prescribedDrugRepository.save(PrescribedDrug.builder()
                    .prescription(prescription)
                    .drug(drugQueryRepository.findById(cpmDto.drugId()))
                    .quantity(cpmDto.quantity())
                    .frequency(cpmDto.frequency())
                    .day(cpmDto.day())
                    .direction(cpmDto.direction())
                    .build()
            );
        }

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

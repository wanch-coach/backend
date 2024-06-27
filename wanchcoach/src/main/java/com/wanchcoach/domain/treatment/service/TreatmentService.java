package com.wanchcoach.domain.treatment.service;

import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.domain.family.entity.Family;
import com.wanchcoach.domain.family.repository.query.FamilyQueryRepository;
import com.wanchcoach.domain.medical.entity.Hospital;
import com.wanchcoach.domain.medical.entity.Pharmacy;
import com.wanchcoach.domain.medical.repository.query.HospitalQueryRepository;
import com.wanchcoach.domain.medical.repository.query.PharmacyQueryRepository;
import com.wanchcoach.domain.drug.repository.query.DrugQueryRepository;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.domain.treatment.controller.dto.response.*;
import com.wanchcoach.domain.treatment.entity.PrescribedDrug;
import com.wanchcoach.domain.treatment.entity.Prescription;
import com.wanchcoach.domain.treatment.entity.Treatment;
import com.wanchcoach.domain.treatment.repository.command.PrescribedDrugRepository;
import com.wanchcoach.domain.treatment.repository.command.PrescriptionRepository;
import com.wanchcoach.domain.treatment.repository.command.TreatmentRepository;
import com.wanchcoach.domain.treatment.repository.query.PrescribedDrugQueryRepository;
import com.wanchcoach.domain.treatment.repository.query.PrescriptionQueryRepository;
import com.wanchcoach.domain.treatment.repository.query.TreatmentQueryRepository;
import com.wanchcoach.domain.treatment.service.dto.*;
import com.wanchcoach.global.error.InvalidAccessException;
import com.wanchcoach.global.service.UploadFileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private final MemberRepository memberQueryRepository;
    private final FamilyQueryRepository familyQueryRepository;
    private final DrugQueryRepository drugQueryRepository;

    private final UploadFileService uploadFileService;
    private final TreatmentQueryRepository treatmentQueryRepository;
    private final PrescriptionQueryRepository prescriptionQueryRepository;
    private final PrescribedDrugQueryRepository prescribedDrugQueryRepository;

    /**
     * 진료 등록 메서드
     *
     * @param dto 등록할 진료 정보
     *
     */
    public CreateTreatmentResponse createTreatment(Long memberId, CreateTreatmentDto dto) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Family family = familyQueryRepository.findById(dto.familyId());
        checkValidAccess(member, family);

        Hospital hospital = hospitalQueryRepository.findById(dto.hospitalId());

        // 저장
        Treatment savedTreatment = treatmentRepository.save(dto.toEntity(family, hospital));

        // 진료 id 반환
        return new CreateTreatmentResponse(savedTreatment.getTreatmentId(), null);
    }

    /**
     * 처방전 등록 메서드
     *
     * @param dto 등록할 처방전(복용할 약) 정보
     */
    public CreatePrescriptionResponse createPrescription(Long memberId, CreatePrescriptionDto dto, Long treatmentId, MultipartFile file) throws Exception {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findById(treatmentId);
        checkValidAccess(member, treatment);

        // 남은 복약 횟수 계산
        int maxRemains = 0;
        for (CreatePrescribedDrugDto cpmDto: dto.prescribedDrugs()) {
            maxRemains = Math.max(maxRemains, cpmDto.frequency() * cpmDto.day());
        }

        // 처방전 저장
        // todo: 전체 복약횟수 계산 로직 개선
        Pharmacy pharmacy = pharmacyQueryRepository.findById(dto.pharmacyId());
        Prescription prescription = prescriptionRepository.save(dto.toEntity(pharmacy, maxRemains));

        // 진료 처방전 정보 저장
        treatment.updatePrescription(prescription);


        // 처방받은 약 저장
        for (CreatePrescribedDrugDto cpmDto : dto.prescribedDrugs()) {
            Drug drug = drugQueryRepository.findById(cpmDto.drugId());
            prescribedDrugRepository.save(cpmDto.toEntity(prescription, drug));
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
            5. 알림 여부는 prescription.morning, prescription.noon, prescription.evening, prescription.beforeBed 값으로 가져오기
         */

        // 처방전 이미지 저장
        if (file != null) {
            Family family = familyQueryRepository.findById(dto.familyId());
            String fileName = family.getName() +
                    "_" +
                    prescription.getPrescriptionId() +
                    "_" +
                    prescription.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                    "." +
                    StringUtils.getFilenameExtension(file.getOriginalFilename());

            prescription.updateImageFileName(fileName);
            fileName = "prescription/" + dto.familyId() + "/" + fileName;
            uploadFileService.uploadPrescriptionImage(file, fileName);
        }

        // 처방전 id 반환
        return new CreatePrescriptionResponse(prescription.getPrescriptionId());
    }

    /**
     * 진료 여부 변경 메서드
     *
     * @param treatmentId 진료 ID
     * @return 진료 여부 변경한 진료 ID 및 변경된 진료 여부 값
     */
    public TakeTreatmentResponse takeTreatment(Long memberId, Long treatmentId) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findById(treatmentId);
        checkValidAccess(member, treatment);

        return new TakeTreatmentResponse(treatment.getTreatmentId(), treatment.updateTaken());
    }

    /**
     * 진료 예약 알람 여부 변경 메서드
     *
     * @param treatmentId 진료 ID
     * @return 진료 예약 알람 여부 변경한 진료 ID 및 변경된 진료 예약 알림 여부 값
     */
    public SetTreatmentAlarmResponse setTreatmentAlarm(Long memberId, Long treatmentId) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findById(treatmentId);
        checkValidAccess(member, treatment);

        return new SetTreatmentAlarmResponse(treatment.getTreatmentId(), treatment.updateAlarm());
    }

    /**
     * 진료 정보 수정 메서드
     *
     * @param dto 수정할 진료 정보
     * @return 수정한 진료 ID
     *
     */
    public UpdateTreatmentResponse modifyTreatment(Long memberId, UpdateTreatmentDto dto) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findById(dto.treatmentId());
        checkValidAccess(member, treatment);

        Family family = familyQueryRepository.findById(dto.familyId());
        Hospital hospital = hospitalQueryRepository.findById(dto.hospitalId());
        treatment.update(dto.toEntity(family, hospital));

        if (dto.prescription() != null) {
            // 처방전 수정
            Long prescriptionId = treatment.getPrescription().getPrescriptionId();
            Prescription prescription = prescriptionQueryRepository.findById(prescriptionId);
            Pharmacy pharmacy = pharmacyQueryRepository.findById(dto.prescription().pharmacyId());
            prescription.update(dto.prescription().toEntity(pharmacy));

            if (dto.prescription().prescribedDrugs() != null) {
                List<PrescribedDrug> prescribedDrugs = prescribedDrugQueryRepository.findByPrescriptionId(prescriptionId);

                prescribedDrugRepository.deleteAll(prescribedDrugs);

                List<UpdatePrescribedDrugDto> prescribedDrugDtoList = dto.prescription().prescribedDrugs();
                for (UpdatePrescribedDrugDto prescribedDrugDto : prescribedDrugDtoList) {
                    Drug drug = drugQueryRepository.findById(prescribedDrugDto.drugId());
                    prescribedDrugRepository.save(prescribedDrugDto.toEntity(prescription, drug));
                    // todo: morning, noon, evening, beforeBed 값에 따라 복약 예정 다시 만들어야 함
                }
            }
        }

        return new UpdateTreatmentResponse(dto.treatmentId(), dto.prescription() == null ? null : dto.prescription().prescriptionId());
    }

    /**
     * 진료 정보 삭제 메서드
     *
     * @param dto 삭제할 진료 ID
     * @return 삭제한 진료 ID
     */
    public DeleteTreatmentResponse deleteTreatment(Long memberId, DeleteTreatmentDto dto) {
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findById(dto.treatmentId());
        checkValidAccess(member, treatment);
        treatment.delete();

        Prescription prescription = prescriptionQueryRepository.findById(treatment.getPrescription().getPrescriptionId());
        // todo: 현재 처방전 active = false: 진료 처방전 삭제 로직 완성 시 수정
        if (prescription != null) prescription.delete();

        // todo: 해당 진료와 연관된 복약 예정 건 삭제

        return new DeleteTreatmentResponse(treatment.getTreatmentId());
    }

    /**
     * @param prescriptionId 복약 종료할 처방전 ID
     */
    public void endPrescription(Long memberId, Long prescriptionId){
        Member member = memberQueryRepository.findByMemberId(memberId);
        Treatment treatment = treatmentQueryRepository.findByPrescriptionId(prescriptionId);
        checkValidAccess(member, treatment);

        Prescription prescription = treatment.getPrescription();
        prescription.updateTakingAndEndDate(false, LocalDate.now());
    }

    private void checkValidAccess(Member member, Family family) {
        if (!family.getMember().getMemberId().equals(member.getMemberId())) {
            throw new InvalidAccessException("잘못된 접근입니다.");
        }
    }

    private void checkValidAccess(Member member, Treatment treatment) {
        if (!treatment.getFamily().getMember().getMemberId().equals(member.getMemberId())) {
            throw new InvalidAccessException("잘못된 접근입니다.");
        }
    }
}

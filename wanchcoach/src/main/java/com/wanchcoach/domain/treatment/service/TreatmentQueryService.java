package com.wanchcoach.domain.treatment.service;

import com.wanchcoach.domain.treatment.repository.query.TreatmentQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 진료 조회 서비스
 * 
 * @author 박은규
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TreatmentQueryService {
    private final TreatmentQueryRepository treatmentQueryRepository;
    /**
     * 진료 조회 API
     */
}

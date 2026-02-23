package com.personalized_learning_hub.service.user;

import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentRequest;
import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentResponse;

import java.util.List;

public interface SkillAssessmentService {

    // === CRUD ===

    List<DtoSkillAssessmentResponse> getSkillAssessmentsByStudentId(Long studentId);
    DtoSkillAssessmentResponse createSkillAssessment(DtoSkillAssessmentRequest dto, Long studentId);
    void delete(Long id);
}

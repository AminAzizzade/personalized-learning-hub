package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentRequest;
import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentResponse;
import com.personalized_learning_hub.entity.SkillAssessment;
import com.personalized_learning_hub.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillAssessmentMapper {

    // Request -> Entity
    public SkillAssessment toEntity(DtoSkillAssessmentRequest dto, Student student) {
        SkillAssessment sa = new SkillAssessment();
        sa.setTopic(dto.getTopic());
        sa.setLearningGoal(dto.getLearningGoal());
        sa.setPreferredStyle(dto.getPreferredStyle());
        sa.setPriceRange(dto.getPriceRange());
        sa.setLanguage(dto.getLanguage());
        sa.setStartDate(dto.getStartDate());
        sa.setTotalDuration(dto.getTotalDuration());
        sa.setPreferredTutorRating(dto.getPreferredTutorRating());
        sa.setAvailability(dto.getAvailability());
        sa.setStudent(student);
        return sa;
    }

    // Entity -> Response
    public DtoSkillAssessmentResponse toResponse(SkillAssessment sa) {
        DtoSkillAssessmentResponse dto = new DtoSkillAssessmentResponse();
        dto.setId(sa.getId());
        dto.setTopic(sa.getTopic());
        dto.setLearningGoal(sa.getLearningGoal());
        dto.setPreferredStyle(sa.getPreferredStyle());
        dto.setPriceRange(sa.getPriceRange());
        dto.setLanguage(sa.getLanguage());
        dto.setTotalDuration(sa.getTotalDuration());
        dto.setPreferredTutorRating(sa.getPreferredTutorRating());
        dto.setAvailability(sa.getAvailability());
        return dto;
    }
}



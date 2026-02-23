package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.SkillAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillAssessmentRepository extends JpaRepository<SkillAssessment, Long> {

    List<SkillAssessment> findByStudentId(Long studentId);

}

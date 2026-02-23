package com.personalized_learning_hub.controller.user;

import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentRequest;
import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentResponse;
import com.personalized_learning_hub.service.user.SkillAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skill-assessments")
@RequiredArgsConstructor
public class SkillAssessmentController {

    private final SkillAssessmentService skillAssessmentService;


    // === CRUD İŞLEMLERİ ===

    /** Öğrencinin tüm yetenek değerlendirmelerini getirir */
    @GetMapping("/{studentId}")
    public List<DtoSkillAssessmentResponse> getSkillAssessments(@PathVariable Long studentId) {
        return skillAssessmentService.getSkillAssessmentsByStudentId(studentId);
    }

    /** Yeni yetenek değerlendirmesi oluşturur ve öğretmen atar */
    @PostMapping("/{studentId}")
    public DtoSkillAssessmentResponse createSkillAssessment(
            @PathVariable Long studentId,
            @RequestBody DtoSkillAssessmentRequest request) {
        return skillAssessmentService.createSkillAssessment(request, studentId);
    }

    /** Yetek değerlendirmesini siler */
    @DeleteMapping("/{id}")
    public void deleteSkillAssessment(@PathVariable Long id) {
        skillAssessmentService.delete(id);
    }


}

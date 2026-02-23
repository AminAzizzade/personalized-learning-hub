package com.personalized_learning_hub.service.user.impl;

import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentRequest;
import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentResponse;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.SkillAssessment;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.mapper.SessionMapper;
import com.personalized_learning_hub.mapper.SkillAssessmentMapper;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.repository.SkillAssessmentRepository;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.service.system.TutorAssignmentService;
import com.personalized_learning_hub.service.user.SkillAssessmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SkillAssessmentServiceImpl implements SkillAssessmentService {

    // === DEPENDENCY ===
    private final SkillAssessmentRepository skillAssessmentRepository;
    private final SkillAssessmentMapper skillAssessmentMapper;
    private final StudentRepository studentRepository;
    private final TutorAssignmentService tutorAssignmentService;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;


    // ========== CRUD İŞLEMLERİ ==========

    /** Öğrencinin tüm yetenek değerlendirmelerini DTO olarak getirir */
    @Override
    public List<DtoSkillAssessmentResponse> getSkillAssessmentsByStudentId(Long studentId) {
        List<SkillAssessment> list = skillAssessmentRepository.findByStudentId(studentId);
        return list.stream()
                .map(skillAssessmentMapper::toResponse)
                .toList();
    }

    /** Yeni yetenek değerlendirmesi oluşturur ve uygun öğretmeni atar */
    @Override
    @Transactional
    public DtoSkillAssessmentResponse createSkillAssessment(DtoSkillAssessmentRequest dto, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı"));

        SkillAssessment sa = skillAssessmentMapper.toEntity(dto, student);
        skillAssessmentRepository.save(sa);

        Tutor matchedTutor = tutorAssignmentService.assignBestTutorForSkillAssessment(sa);
        if (matchedTutor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uygun öğretmen bulunamadı.");
        }

        Session session = sessionMapper.fromSkillAssessment(sa, matchedTutor, student);
        sessionRepository.save(session);

        return skillAssessmentMapper.toResponse(sa);
    }

    /** Yetenek değerlendirmesini siler */
    @Override
    public void delete(Long id) {
        skillAssessmentRepository.deleteById(id);
    }
}

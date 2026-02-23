package com.personalized_learning_hub.service.user.impl;

import com.personalized_learning_hub.dto.profil.DtoStudentPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoStudentRequest;
import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.mapper.*;
import com.personalized_learning_hub.repository.*;
import com.personalized_learning_hub.service.system.FileStorageService;
import com.personalized_learning_hub.service.system.TutorAssignmentService;
import com.personalized_learning_hub.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    // === Dependencies ===
    private final StudentRepository studentRepository;
    private final ResourceRepository resourceRepository;
    private final SessionRepository sessionRepository;
    private final SessionReservationRepository reservationRepository;
    private final SkillAssessmentRepository skillAssessmentRepository;
    private final TutorRepository tutorRepository;

    private final StudentMapper studentMapper;
    private final ResourceMapper resourceMapper;
    private final SessionMapper sessionMapper;
    private final SessionReservationMapper reservationMapper;
    private final SkillAssessmentMapper skillAssessmentMapper;
    private final TutorMapper tutorMapper;

    private final FileStorageService fileStorageService;
    private final TutorAssignmentService tutorAssignmentService;


    // ========== CRUD ==========

    /** Öğrenciyi ID ile getirir (entity) */
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    /** Öğrenciyi ID ile siler */
    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    /** DTO olarak öğrenci bilgisi döner */
    @Override
    public DtoStudentResponse getDtoById(Long id) {

        return studentMapper.toResponse(getStudentById(id));
    }

    /** Öğrenci bilgilerini DTO üzerinden günceller */
    @Override
    public DtoStudentResponse updateStudentFromDto(Long id, DtoStudentRequest request) {
        Student existing = getStudentById(id);
        Student updated = studentMapper.toUpdateEntity(request);
        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        existing.setFavoriteTopic(updated.getFavoriteTopic());
        return studentMapper.toResponse(studentRepository.save(existing));
    }



    // ----------- STUDENT İŞLEMLERİ ------------

    @Override
    public DtoStudentPublicResponse getPublicStudentDto(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return studentMapper.toPublicResponse(student);
    }


}

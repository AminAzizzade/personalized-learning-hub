package com.personalized_learning_hub.service.user.impl;

import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorRequest;
import com.personalized_learning_hub.dto.profil.DtoTutorResponse;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.mapper.*;
import com.personalized_learning_hub.repository.*;
import com.personalized_learning_hub.service.user.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    // === DEPENDENCY ===
    private final TutorRepository tutorRepository;
    private final SessionRepository sessionRepository;

    private final TutorMapper tutorMapper;
    private final StudentMapper studentMapper;


    // ----------- CRUD ------------

    @Override
    public DtoTutorResponse getTutorDto(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor not found with id: " + id));
        return tutorMapper.toTutorResponse(tutor);
    }

    @Override
    public DtoTutorResponse updateTutorDto(Long id, DtoTutorRequest dto) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor not found with id: " + id));

        tutor.setName(dto.getName());
        tutor.setExpertiseTopics(dto.getExpertiseTopics());
        tutor.setPreferredStyles(dto.getPreferredStyles());
        tutor.setAvailability(dto.getAvailability());
        tutor.setLanguage(dto.getLanguage());
        tutor.setPricePerHour(dto.getPricePerHour());

        Tutor updated = tutorRepository.save(tutor);
        return tutorMapper.toTutorResponse(updated);
    }

    @Override
    public void deleteTutor(Long id) {
        tutorRepository.deleteById(id);
    }




    // ========== TUTOR İŞLEMLERİ ==========

    /** Belirli bir session'a atanmış öğretmenin uygunluk saatlerini getirir */
    @Override
    public List<String> getAvailability(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Tutor tutor = session.getTutor();
        if (tutor == null) {
            throw new RuntimeException("Session has no assigned tutor.");
        }

        return tutor.getAvailability();
    }

    /** Öğretmene atanmış öğrencileri getirir */
    @Override
    public List<DtoStudentResponse> getAssignedStudentsDto(Long tutorId) {
        List<Student> students = sessionRepository.findDistinctStudentsByTutorId(tutorId);
        return students.stream().map(studentMapper::toResponse).toList();
    }


    /** Öğretmenin öğrenciye açık profilini döner */
    @Override
    public DtoTutorPublicResponse getTutorPublic(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor not found with id: " + id));
        return tutorMapper.toTutorPublicResponse(tutor);
    }

}

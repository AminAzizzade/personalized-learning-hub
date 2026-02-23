package com.personalized_learning_hub.service.user;

import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorRequest;
import com.personalized_learning_hub.dto.profil.DtoTutorResponse;

import java.util.List;

public interface TutorService {


    // ----------- CRUD ------------

    DtoTutorResponse getTutorDto(Long id);
    DtoTutorResponse updateTutorDto(Long id, DtoTutorRequest dto);
    void deleteTutor(Long id);

    // ---------- TUTOR İŞLEMLERİ
    List<String> getAvailability(Long sessionId);
    List<DtoStudentResponse> getAssignedStudentsDto(Long tutorId);
    DtoTutorPublicResponse getTutorPublic(Long id);


}

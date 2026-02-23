package com.personalized_learning_hub.service.user;

import com.personalized_learning_hub.dto.profil.DtoStudentPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoStudentRequest;
import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.entity.Student;

public interface StudentService {


    // === CRUD ===
    Student getStudentById(Long id);
    void deleteStudent(Long id);
    DtoStudentResponse getDtoById(Long id);
    DtoStudentResponse updateStudentFromDto(Long id, DtoStudentRequest request);



    // ----------- Student İŞLEMLERİ ------------

    DtoStudentPublicResponse getPublicStudentDto(Long id);



}

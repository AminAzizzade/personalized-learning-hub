package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.auth.RegisterStudentRequest;
import com.personalized_learning_hub.dto.profil.DtoStudentPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoStudentRequest;
import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toUpdateEntity(DtoStudentRequest dto) {
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setPhone(dto.getPhone());
        student.setFavoriteTopic(dto.getFavoriteTopic());
        return student;
    }

    public Student toEntity(RegisterStudentRequest dto, User user) {
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setEmail(user.getEmail());
        student.setUser(user); // User bağlantısı
        return student;
    }

    // Öğrencinin kendi profilini görmesi
    public DtoStudentResponse toResponse(Student student) {
        DtoStudentResponse dto = new DtoStudentResponse();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setTotalSessionsCompleted(student.getTotalSessionsCompleted());
        dto.setTotalSessionsScore(student.getTotalSessionsScore());
        dto.setFavoriteTopic(student.getFavoriteTopic());
        return dto;
    }

    // Başkalarının göreceği özet profil
    public DtoStudentPublicResponse toPublicResponse(Student student) {
        DtoStudentPublicResponse dto = new DtoStudentPublicResponse();
        dto.setFullName(student.getFullName());
        dto.setTotalSessionsCompleted(student.getTotalSessionsCompleted());
        dto.setTotalSessionsScore(student.getTotalSessionsScore());
        dto.setFavoriteTopic(student.getFavoriteTopic());
        return dto;
    }


}

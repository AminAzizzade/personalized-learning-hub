package com.personalized_learning_hub.controller.user;

import com.personalized_learning_hub.dto.profil.DtoStudentPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoStudentRequest;
import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {


    private final StudentService studentService;

    // === CRUD ===

    /** Öğrenciyi ID’ye göre getirir */
    @GetMapping("/{id}")
    public DtoStudentResponse getStudentById(@PathVariable Long id) {
        return studentService.getDtoById(id);
    }

    /** Öğrenci bilgilerini günceller */
    @PutMapping("/{id}")
    public DtoStudentResponse updateStudent(@PathVariable Long id, @RequestBody DtoStudentRequest request) {
        return studentService.updateStudentFromDto(id, request);
    }

    /** Öğrenciyi siler */
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }



    // ----------- STUDENT İŞLEMLERİ ------------

    // Öğrencinin genel (public) bilgilerini getirir
    @GetMapping("/public/{id}")
    public DtoStudentPublicResponse getPublicStudent(@PathVariable Long id) {
        return studentService.getPublicStudentDto(id);
    }




}

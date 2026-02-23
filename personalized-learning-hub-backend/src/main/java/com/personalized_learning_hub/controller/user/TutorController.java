package com.personalized_learning_hub.controller.user;

import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorRequest;
import com.personalized_learning_hub.dto.profil.DtoTutorResponse;
import com.personalized_learning_hub.service.user.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    // ----------- CRUD ------------

    // Öğretmen bilgilerini getirir
    @GetMapping("/{id}")
    public DtoTutorResponse getTutor(@PathVariable Long id) {
        return tutorService.getTutorDto(id);
    }

    // Öğretmen bilgilerini günceller
    @PutMapping("/{id}")
    public DtoTutorResponse updateTutor(@PathVariable Long id, @RequestBody DtoTutorRequest request) {
        return tutorService.updateTutorDto(id, request);
    }

    // Öğretmeni siler
    @DeleteMapping("/{id}")
    public void deleteTutor(@PathVariable Long id) {
        tutorService.deleteTutor(id);
    }



    // ========== TUTOR İŞLEMLERİ ==========

    // Belirli bir session'a atanmış öğretmenin uygunluk saatlerini getirir
    @GetMapping("/{sessionId}/availabilities")
    public ResponseEntity<List<String>> getTutorAvailability(@PathVariable Long sessionId) {
        return ResponseEntity.ok(tutorService.getAvailability(sessionId));
    }


    // Öğretmene atanmış öğrencileri getirir
    @GetMapping("/assigned-students/{tutorId}")
    public ResponseEntity<List<DtoStudentResponse>> getAssignedStudents(@PathVariable Long tutorId) {
        return ResponseEntity.ok(tutorService.getAssignedStudentsDto(tutorId));
    }



    /** Öğrencinin öğretmeninin herkese açık bilgilerini getirir */
    @GetMapping("/public/{id}")
    public DtoTutorPublicResponse getTutorPublic(@PathVariable Long id) {
        return tutorService.getTutorPublic(id);
    }

}

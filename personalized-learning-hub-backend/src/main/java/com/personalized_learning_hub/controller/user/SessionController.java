package com.personalized_learning_hub.controller.user;

import com.personalized_learning_hub.dto.session.DtoProgressWrapper;
import com.personalized_learning_hub.dto.session.DtoSessionRequest;
import com.personalized_learning_hub.dto.session.DtoSessionResponse;
import com.personalized_learning_hub.service.user.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // === CRUD İŞLEMLERİ ===

    /** Oturumun durumunu güncelle */
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateSessionStatus(@PathVariable Long id, @RequestBody DtoSessionRequest request) {
        sessionService.updateSessionStatus(id, request);
        return ResponseEntity.ok("Session status updated.");
    }

    /** Oturumu sil */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }



    // === STUDENT İŞLEMLERİ ===

    /** Öğrenciye ait tüm oturumları getirir */
    @GetMapping("/student/{studentId}")
    public List<DtoSessionResponse> getSessionsByStudentId(@PathVariable Long studentId) {
        return sessionService.getSessionsByStudentId(studentId);
    }

    /** Oturumlara ait kaynak/rezervasyon bazlı ilerlemeyi getirir */
    @GetMapping("/student/{studentId}/progress")
    public List<DtoProgressWrapper> getSessionProgressByStudentId(@PathVariable Long studentId) {
        return sessionService.getSessionProgressByStudentId(studentId);
    }



    // === TUTOR İŞLEMLERİ ===

    /** Öğretmenin onay bekleyen oturumlarını getirir */
    @GetMapping("/tutor/{tutorId}/pending")
    public ResponseEntity<List<DtoSessionResponse>> getPendingSessions(@PathVariable Long tutorId) {
        return ResponseEntity.ok(sessionService.getPendingSessionsDto(tutorId));
    }

    /** Belirli bir oturumu onaylar */
    @PutMapping("/tutor/{sessionId}/approve")
    public ResponseEntity<Void> approveSession(@PathVariable Long sessionId) {
        sessionService.approveSession(sessionId);
        return ResponseEntity.ok().build();
    }

    /** Belirli bir oturumu iptal eder */
    @PutMapping("/tutor/{sessionId}/cancel")
    public ResponseEntity<Void> cancelSession(@PathVariable Long sessionId) {
        sessionService.cancelSession(sessionId);
        return ResponseEntity.ok().build();
    }

    /** Öğretmenin onaylanmış oturumlarını getirir */
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<DtoSessionResponse>> getSessionsByTutorId(@PathVariable Long tutorId) {
        return ResponseEntity.ok(sessionService.getSessionsByTutorId(tutorId));
    }


    /** Öğretmenin tüm öğrencilerine ait ilerlemeleri getirir */
    @GetMapping("/tutor/{tutorId}/progress")
    public ResponseEntity<List<DtoProgressWrapper>> getSessionProgressByTutorId(@PathVariable Long tutorId) {
        return ResponseEntity.ok(sessionService.getSessionProgressByTutorId(tutorId));
    }

}

package com.personalized_learning_hub.controller.user;

import com.personalized_learning_hub.dto.session.DtoSessionReservationRequest;
import com.personalized_learning_hub.dto.session.DtoSessionReservationResponse;
import com.personalized_learning_hub.service.user.SessionReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class SessionReservationController {

    private final SessionReservationService reservationService;


    // CRUD İŞLEMLERİ

    /** Yeni rezervasyon oluştur */
    @PostMapping("")
    public ResponseEntity<DtoSessionReservationResponse> createReservation(
            @RequestBody DtoSessionReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    /** Rezervasyonu sil */
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    /** Rezervasyonu güncelle */
    @PutMapping("/{reservationId}")
    public ResponseEntity<DtoSessionReservationResponse> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody DtoSessionReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(reservationId, request));
    }





    // ========== STUDENT İŞLEMLERİ ==========

    /** Öğrencinin yaptığı tüm rezervasyonları getirir */
    @GetMapping("/student/{studentId}")
    public List<DtoSessionReservationResponse> getReservations(@PathVariable Long studentId) {
        return reservationService.getReservationsByStudent(studentId);
    }


    // ----------- TUTOR İŞLEMLERİ ------------

    // Öğretmenin tüm rezervasyonlarını getirir
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<DtoSessionReservationResponse>> getReservationsByTutor(@PathVariable Long tutorId) {
        return ResponseEntity.ok(reservationService.getReservationsByTutor(tutorId));
    }
}

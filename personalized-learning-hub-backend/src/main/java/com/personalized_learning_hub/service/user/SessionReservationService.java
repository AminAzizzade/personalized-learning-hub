package com.personalized_learning_hub.service.user;

import com.personalized_learning_hub.dto.session.DtoSessionReservationRequest;
import com.personalized_learning_hub.dto.session.DtoSessionReservationResponse;

import java.util.List;

public interface SessionReservationService {


    // ========== CRUD İŞLEMLERİ ==========


    public DtoSessionReservationResponse createReservation(DtoSessionReservationRequest request);
    void deleteReservation(Long reservationId);
    DtoSessionReservationResponse updateReservation(Long reservationId, DtoSessionReservationRequest request);



    // ----------- TUTOR İŞLEMLERİ ------------

    List<DtoSessionReservationResponse> getReservationsByTutor(Long tutorId);



    // =========== STUDENT İŞLEMLERİ =============
    List<DtoSessionReservationResponse> getReservationsByStudent(Long studentId);


}

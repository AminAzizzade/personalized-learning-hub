package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.session.DtoSessionReservationRequest;
import com.personalized_learning_hub.dto.session.DtoSessionReservationResponse;
import com.personalized_learning_hub.entity.SessionReservation;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.service.system.DataTimeUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionReservationMapper {

    private final SessionRepository sessionRepository;
    private final DataTimeUtilService dataTimeUtilService;

    public SessionReservation toEntity(DtoSessionReservationRequest dto) {
        SessionReservation reservation = new SessionReservation();

        // DTO'dan gelen sessionId ile session nesnesi oluşturulup bağlanır
        reservation.setSession(sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + dto.getSessionId())));

        // Diğer alanlar atanır
        reservation.setDateTime(dataTimeUtilService.convertToLocalDateTimeService(dto.getDateTime()));
        reservation.setStatus(dto.getStatus());
        reservation.setScore(dto.getScore());
        reservation.setTutorEvaluationRating(dto.getTutorEvaluationRating());

        return reservation;
    }



    public DtoSessionReservationResponse toDto(SessionReservation sr) {
        DtoSessionReservationResponse dto = new DtoSessionReservationResponse();
        dto.setId(sr.getId());
        dto.setStatus(sr.getStatus());
        dto.setDateTime(sr.getDateTime());
        dto.setScore(sr.getScore());
        dto.setTutorEvaluationRating(sr.getTutorEvaluationRating());
        dto.setSessionId(sr.getSession().getId());
        dto.setPaymentId(sr.getPayment() != null ? sr.getPayment().getId() : null);


        // 👇 topic bilgisini de DTO'ya ekle
        dto.setSessionTopic(sr.getSession().getTopic());
        return dto;
    }

    public void updateEntity(SessionReservation reservation, DtoSessionReservationRequest dto) {
        if (dto.getDateTime() != null) reservation.setDateTime(dataTimeUtilService.convertToLocalDateTimeService(dto.getDateTime()));
        if (dto.getStatus() != null) reservation.setStatus(dto.getStatus());
        if (dto.getScore() != null) reservation.setScore(dto.getScore());
        if (dto.getTutorEvaluationRating() != null) reservation.setTutorEvaluationRating(dto.getTutorEvaluationRating());
    }
}


package com.personalized_learning_hub.dto.session;

import com.personalized_learning_hub.enums.SessionReservationStatus;
import lombok.Data;

@Data
public class DtoSessionReservationRequest {
    private Long sessionId;
    private String dateTime;
    private SessionReservationStatus status;
    private Integer score;
    private Integer tutorEvaluationRating;
}

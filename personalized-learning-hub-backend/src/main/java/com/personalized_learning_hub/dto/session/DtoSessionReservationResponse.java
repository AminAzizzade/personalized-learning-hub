package com.personalized_learning_hub.dto.session;

import com.personalized_learning_hub.enums.SessionReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DtoSessionReservationResponse {
    private Long id;
    private SessionReservationStatus status;
    private LocalDateTime dateTime;
    private int score;
    private int tutorEvaluationRating;
    private Long sessionId;
    private Long paymentId; // null olabilir
    private String sessionTopic;

}

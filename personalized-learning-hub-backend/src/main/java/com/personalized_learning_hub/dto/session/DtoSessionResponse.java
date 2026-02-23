package com.personalized_learning_hub.dto.session;

import com.personalized_learning_hub.enums.SessionStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoSessionResponse {
    private Long id;
    private SessionStatus status;
    private String topic;
    private String totalDuration;
    private int hourlyRate;
    private String language;
    private Date startDate;
    private Date endDate;
    private int totalMeetings;
    private int totalPrice;
    private int finalScore;
    private int totalAbsence;
    private Long studentId;
    private Long tutorId;
    private String studentName;
}


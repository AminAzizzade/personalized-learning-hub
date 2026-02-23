package com.personalized_learning_hub.dto.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoSessionProgressResponse {
    private Long sessionId;
    private int totalMeetings;
    private int finalScore;
    private int totalAbsence;
    private String totalDuration;
}



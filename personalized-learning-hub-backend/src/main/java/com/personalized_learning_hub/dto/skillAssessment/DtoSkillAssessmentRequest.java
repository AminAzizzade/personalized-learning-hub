package com.personalized_learning_hub.dto.skillAssessment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoSkillAssessmentRequest {

    private String topic;
    private String learningGoal;
    private String preferredStyle;
    private String priceRange;
    private String language;
    private Date startDate;
    private String totalDuration;
    private int preferredTutorRating;
    private List<String> availability;

}

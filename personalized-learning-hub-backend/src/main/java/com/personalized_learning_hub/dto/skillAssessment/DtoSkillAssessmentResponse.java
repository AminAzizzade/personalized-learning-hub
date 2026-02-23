package com.personalized_learning_hub.dto.skillAssessment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoSkillAssessmentResponse {

    private Long id;
    private String topic;
    private String learningGoal;
    private String preferredStyle;
    private String language;
    private String priceRange;
    private String totalDuration;
    private int preferredTutorRating;
    private List<String> availability;

}

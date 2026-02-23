package com.personalized_learning_hub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private String preferredStyle;
    private String language;
    private int preferredTutorRating;
    private String priceRange;

    private String learningGoal;
    private String totalDuration;

    @Temporal(TemporalType.DATE)
    private Date startDate;


    @ElementCollection
    private List<String> availability; // örn: "Pazartesi,09:00-10:00"

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}


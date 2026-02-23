package com.personalized_learning_hub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName = "";
    private String email = "";
    private String phone = "";

    @Column(nullable = false)
    private int totalSessionsCompleted = 0;

    @Column(nullable = false)
    private int totalSessionsScore = 0;

    private String favoriteTopic = "";

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillAssessment> assessments = List.of();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = List.of();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}

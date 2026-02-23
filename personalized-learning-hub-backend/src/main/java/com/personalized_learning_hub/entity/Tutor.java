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
public class Tutor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "";
    private String email = "";

    @ElementCollection
    private List<String> expertiseTopics = List.of();

    @ElementCollection
    private List<String> preferredStyles = List.of();

    // private String language = "";
    @ElementCollection
    private List<String> language = List.of(); // veya new ArrayList<>()

    private int pricePerHour = 0;

    private double rating = 0; // 1-5 arası, varsayılan 0

    @Column(nullable = false)
    private int totalMeeting = 0;

    @Column(nullable = false)
    private int totalStudent = 0;

    @ElementCollection
    private List<String> availability = List.of();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = List.of();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

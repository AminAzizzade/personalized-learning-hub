package com.personalized_learning_hub.dto.profil;

import lombok.Data;

import java.util.List;


@Data
public class DtoTutorResponse {

    private Long id;
    private String name;
    private String email;
    private List<String> expertiseTopics;
    private List<String> preferredStyles;
    private List<String> availability;
    private List<String> language;
    private int pricePerHour;
    private double rating;
    private int totalMeeting;
    private int totalStudent;
}

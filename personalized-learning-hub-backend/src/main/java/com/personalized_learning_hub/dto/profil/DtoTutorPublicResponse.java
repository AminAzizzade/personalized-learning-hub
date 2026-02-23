package com.personalized_learning_hub.dto.profil;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoTutorPublicResponse {

    private Long id;
    private String name;
    private List<String> expertiseTopics;
    private List<String> preferredStyles;
    private List<String> language;
    private int pricePerHour;
    private double rating;
    private int totalMeeting;
    private int totalStudent;

}

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
public class DtoTutorRequest {


    private String name;
    private List<String> expertiseTopics;
    private List<String> preferredStyles;
    private List<String> availability;
    private List<String> language;
    private int pricePerHour;


}

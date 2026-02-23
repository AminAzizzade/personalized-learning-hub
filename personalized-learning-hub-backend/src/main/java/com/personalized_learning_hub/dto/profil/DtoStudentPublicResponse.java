package com.personalized_learning_hub.dto.profil;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoStudentPublicResponse {


    private String fullName;
    private int totalSessionsCompleted;
    private int totalSessionsScore;
    private String favoriteTopic;


}

package com.personalized_learning_hub.dto.profil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoStudentRequest {

    private String fullName;
    private String phone;
    private String favoriteTopic;
}

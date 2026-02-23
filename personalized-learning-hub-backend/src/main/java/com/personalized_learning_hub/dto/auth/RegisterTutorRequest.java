package com.personalized_learning_hub.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTutorRequest {

    private String fullName;
    private String email;
    private String password;
}

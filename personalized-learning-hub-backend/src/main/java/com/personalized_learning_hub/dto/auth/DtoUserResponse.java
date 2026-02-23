package com.personalized_learning_hub.dto.auth;

import com.personalized_learning_hub.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;

    private Long studentId;
    private Long tutorId;
}

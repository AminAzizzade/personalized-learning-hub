package com.personalized_learning_hub.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCredentialsRequest {
    private String oldPassword;     // doğrulama için
    private String newPassword;     // güncellenecek şifre
    private String newEmail;        // güncellenecek e-posta
}

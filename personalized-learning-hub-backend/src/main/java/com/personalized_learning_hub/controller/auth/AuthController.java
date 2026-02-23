package com.personalized_learning_hub.controller.auth;

import com.personalized_learning_hub.dto.auth.*;
import com.personalized_learning_hub.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    // Kullanıcı giriş işlemi
    @PostMapping("/login")
    public ResponseEntity<DtoUserResponse> login(@RequestBody LoginRequest request) {
        DtoUserResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Yeni öğrenci kaydı
    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@RequestBody RegisterStudentRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok("Öğrenci kaydı başarılı");
    }

    // Yeni öğretmen kaydı
    @PostMapping("/register/tutor")
    public ResponseEntity<String> registerTutor(@RequestBody RegisterTutorRequest request) {
        authService.registerTutor(request);
        return ResponseEntity.ok("Tutor kaydı başarılı");
    }

    // Şifre veya e-posta güncelleme
    @PutMapping("/{userId}/credentials")
    public void updateCredentials(@PathVariable Long userId, @RequestBody UpdateCredentialsRequest request) {
        authService.updateCredentials(userId, request);
    }
}

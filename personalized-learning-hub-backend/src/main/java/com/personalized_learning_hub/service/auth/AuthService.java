package com.personalized_learning_hub.service.auth;

import com.personalized_learning_hub.dto.auth.*;

public interface AuthService {

    public void registerTutor(RegisterTutorRequest request);

    public void registerStudent(RegisterStudentRequest request);

    public DtoUserResponse login(LoginRequest request);

    void updateCredentials(Long userId, UpdateCredentialsRequest request);
}

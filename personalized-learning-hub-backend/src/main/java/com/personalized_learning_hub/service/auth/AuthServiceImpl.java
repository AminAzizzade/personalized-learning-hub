package com.personalized_learning_hub.service.auth;

import com.personalized_learning_hub.dto.auth.*;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.entity.User;
import com.personalized_learning_hub.enums.Role;
import com.personalized_learning_hub.mapper.StudentMapper;
import com.personalized_learning_hub.mapper.TutorMapper;
import com.personalized_learning_hub.mapper.UserMapper;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.repository.TutorRepository;
import com.personalized_learning_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final TutorMapper tutorMapper;

    // Kullanıcıyı e-posta ve şifre ile giriş yaparak doğrular
    @Override
    public DtoUserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Şifre yanlış");
        }

        DtoUserResponse dto = userMapper.toDto(user);

        if (user.getRole().equals(Role.STUDENT)) {
            Student student = studentRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı"));
            dto.setStudentId(student.getId());
        }

        if (user.getRole().equals(Role.TUTOR)) {
            Tutor tutor = tutorRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Tutor bulunamadı"));
            dto.setTutorId(tutor.getId());
        }

        return dto;
    }

    // Öğrenci kaydını gerçekleştirir (User + Student oluşturur)
    @Override
    public void registerStudent(RegisterStudentRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = userMapper.toEntity(request, encodedPassword);
        Student student = studentMapper.toEntity(request, user);
        userRepository.save(user);
        studentRepository.save(student);
    }

    // Tutor kaydını gerçekleştirir (User + Tutor oluşturur)
    @Override
    public void registerTutor(RegisterTutorRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = userMapper.toEntity(request, encodedPassword);
        Tutor tutor = tutorMapper.toEntity(request, user);
        userRepository.save(user);
        tutorRepository.save(tutor);
    }

    // Kullanıcıya ait şifre veya e-posta bilgilerini günceller
    @Override
    public void updateCredentials(Long userId, UpdateCredentialsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Eski şifre hatalı");
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        if (request.getNewEmail() != null && !request.getNewEmail().isBlank()) {
            user.setEmail(request.getNewEmail());
        }

        userRepository.save(user);
    }

}

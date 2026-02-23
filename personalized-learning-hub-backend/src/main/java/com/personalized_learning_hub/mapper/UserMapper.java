package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.auth.DtoUserResponse;
import com.personalized_learning_hub.dto.auth.RegisterStudentRequest;
import com.personalized_learning_hub.dto.auth.RegisterTutorRequest;
import com.personalized_learning_hub.entity.User;
import com.personalized_learning_hub.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Entity → DTO
    public DtoUserResponse toDto(User user) {
        if (user == null) return null;

        DtoUserResponse dto = new DtoUserResponse();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }

    // DTO → Entity (genelde kayıt için kullanılır)
    public User toEntity(DtoUserResponse dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId()); // genelde null olur, ama DTO’da varsa alınır
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }

    // RegisterStudentRequest → User Entity
    public User toEntity(RegisterStudentRequest request, String encodedPassword) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(Role.STUDENT);
        return user;
    }

    // RegisterTutorRequest → User Entity
    public User toEntity(RegisterTutorRequest request, String encodedPassword) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(Role.TUTOR);
        return user;
    }

}

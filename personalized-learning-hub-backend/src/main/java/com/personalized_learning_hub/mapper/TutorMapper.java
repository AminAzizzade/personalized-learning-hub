package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.auth.RegisterTutorRequest;
import com.personalized_learning_hub.dto.profil.DtoTutorPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorRequest;
import com.personalized_learning_hub.dto.profil.DtoTutorResponse;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TutorMapper {


    public Tutor toUpdateEntity(DtoTutorRequest dto) {
        Tutor tutor = new Tutor();
        tutor.setName(dto.getName());
        tutor.setExpertiseTopics(dto.getExpertiseTopics());
        tutor.setPreferredStyles(dto.getPreferredStyles());
        tutor.setAvailability(dto.getAvailability());
        tutor.setLanguage(dto.getLanguage());
        tutor.setPricePerHour(dto.getPricePerHour());
        return tutor;
    }

    public Tutor toEntity(RegisterTutorRequest dto, User user) {
        Tutor tutor = new Tutor();
        tutor.setName(dto.getFullName());
        tutor.setEmail(user.getEmail());
        tutor.setUser(user);
        return tutor;
    }

    // Öğrencilerin göreceği profil
    public DtoTutorPublicResponse toTutorPublicResponse(Tutor tutor) {
        DtoTutorPublicResponse dto = new DtoTutorPublicResponse();
        dto.setId(tutor.getId());
        dto.setName(tutor.getName());
        dto.setExpertiseTopics(tutor.getExpertiseTopics());
        dto.setPreferredStyles(tutor.getPreferredStyles());
        dto.setLanguage(tutor.getLanguage());
        dto.setPricePerHour(tutor.getPricePerHour());
        dto.setRating(tutor.getRating());
        dto.setTotalMeeting(tutor.getTotalMeeting());
        dto.setTotalStudent(tutor.getTotalStudent());
        return dto;
    }

    // Öğretmenin kendi profiline erişmesi
    public DtoTutorResponse toTutorResponse(Tutor tutor) {
        DtoTutorResponse dto = new DtoTutorResponse();
        dto.setId(tutor.getId());
        dto.setName(tutor.getName());
        dto.setEmail(tutor.getEmail());
        dto.setExpertiseTopics(tutor.getExpertiseTopics());
        dto.setPreferredStyles(tutor.getPreferredStyles());
        dto.setAvailability(tutor.getAvailability());
        dto.setLanguage(tutor.getLanguage());
        dto.setPricePerHour(tutor.getPricePerHour());
        dto.setRating(tutor.getRating());
        dto.setTotalMeeting(tutor.getTotalMeeting());
        dto.setTotalStudent(tutor.getTotalStudent());

        return dto;
    }


}


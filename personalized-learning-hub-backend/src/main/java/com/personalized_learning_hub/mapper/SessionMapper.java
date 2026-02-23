package com.personalized_learning_hub.mapper;


import com.personalized_learning_hub.dto.session.DtoProgressResponse;
import com.personalized_learning_hub.dto.session.DtoSessionProgressResponse;
import com.personalized_learning_hub.dto.session.DtoSessionResponse;
import com.personalized_learning_hub.entity.*;
import com.personalized_learning_hub.enums.SessionStatus;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.repository.TutorRepository;
import com.personalized_learning_hub.service.system.DataTimeUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class SessionMapper {

    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final DataTimeUtilService dataTimeUtilService;

    // Detaylı görünüm için (Entity -> DtoSessionResponse)
    public DtoSessionResponse toResponse(Session session) {
        DtoSessionResponse dto = new DtoSessionResponse();
        dto.setId(session.getId());
        dto.setStatus(session.getStatus());
        dto.setTopic(session.getTopic());
        dto.setTotalDuration(session.getTotalDuration());
        dto.setHourlyRate(session.getHourlyRate());
        dto.setLanguage(session.getLanguage());
        dto.setStartDate(session.getStartDate());
        dto.setEndDate(session.getEndDate());
        dto.setTotalMeetings(session.getTotalMeetings());
        dto.setTotalPrice(session.getTotalPrice());
        dto.setFinalScore(session.getFinalScore());
        dto.setTotalAbsence(session.getTotalAbsence());
        dto.setStudentId(session.getStudent().getId());
        dto.setTutorId(session.getTutor().getId());

        dto.setStudentName(session.getStudent().getFullName());
        return dto;
    }

    // Progress Tracking görünümü için (Entity -> DtoSessionProgressResponse)
    public DtoSessionProgressResponse toProgressResponse(Session session) {
        return new DtoSessionProgressResponse(
                session.getId(),
                session.getTotalMeetings(),
                session.getFinalScore(),
                session.getTotalAbsence(),
                session.getTotalDuration()
        );
    }

    public Session fromSkillAssessment(SkillAssessment sa, Tutor tutor, Student student) {
        Session session = new Session();
        session.setTopic(sa.getTopic());
        session.setLanguage(sa.getLanguage());
        session.setHourlyRate(tutor.getPricePerHour());
        session.setTotalDuration(sa.getTotalDuration());
        session.setTutor(tutor);
        session.setStudent(student);
        session.setStatus(SessionStatus.PENDING_APPROVAL);
        session.setTotalMeetings(0);
        session.setFinalScore(0);
        session.setTotalAbsence(0);
        session.setTotalPrice(0);

        // Yeni eklenen tarih alanlarını set et
        session.setStartDate(sa.getStartDate());

        // endDate hesaplama
        Date endDate = dataTimeUtilService.calculateEndDate(sa.getStartDate(), sa.getTotalDuration());
        session.setEndDate(endDate);

        return session;
    }

    public DtoProgressResponse ResourceToProgress(Resource resource)
    {
        return new DtoProgressResponse(
                resource.getResourceName(),
                "resource",
                resource.getHomeWorkScore(),
                resource.getDeadLine()
        );
    }

    public DtoProgressResponse ReservationToProgress(SessionReservation reservation)
    {
        return new DtoProgressResponse(
                "",
                "reservation",
                reservation.getScore(),
                dataTimeUtilService.calculateDate(reservation.getDateTime())
        );
    }


}


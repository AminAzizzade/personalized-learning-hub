package com.personalized_learning_hub.service.user.impl;

import com.personalized_learning_hub.dto.session.DtoProgressResponse;
import com.personalized_learning_hub.dto.session.DtoProgressWrapper;
import com.personalized_learning_hub.dto.session.DtoSessionRequest;
import com.personalized_learning_hub.dto.session.DtoSessionResponse;
import com.personalized_learning_hub.entity.*;
import com.personalized_learning_hub.enums.SessionStatus;
import com.personalized_learning_hub.mapper.SessionMapper;
import com.personalized_learning_hub.repository.*;
import com.personalized_learning_hub.service.user.SessionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    // === DEPENDENCY ===
    private final SessionRepository sessionRepository;
    private final SessionReservationRepository reservationRepository;
    private final TutorRepository tutorRepository;
    private final SessionMapper sessionMapper;
    private final ResourceRepository resourceRepository;
    private final StudentRepository studentRepository;



    // ============== CRUD İŞLEMLERİ ================

    /** Oturum durumunu günceller */
    @Override
    public void updateSessionStatus(Long sessionId, DtoSessionRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(request.getStatus());

        if(request.getStatus() == SessionStatus.CONFIRMED){

            Student student = session.getStudent();
            student.setTotalSessionsCompleted(student.getTotalSessionsCompleted() + 1);

        }

        sessionRepository.save(session);
    }

    /** Oturumu siler */
    @Override
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }






    // ============= STUDENT İŞLEMLERİ =================

    /** Öğrenciye ait tüm oturumları getirir (DTO) */
    @Override
    public List<DtoSessionResponse> getSessionsByStudentId(Long studentId) {
        List<Session> sessions = sessionRepository.findByStudentId(studentId);
        return sessions.stream()
                .map(sessionMapper::toResponse)
                .toList();
    }

    /** Oturumlara göre kaynak ve rezervasyon ilerlemelerini getirir */
    @Override
    public List<DtoProgressWrapper> getSessionProgressByStudentId(Long studentId) {
        List<Resource> resources = resourceRepository.findResourcesByStudentId(studentId);
        List<SessionReservation> reservations = reservationRepository.findBySession_Student_Id(studentId);

        Map<Long, DtoProgressWrapper> wrapperMap = new HashMap<>();

        for (Resource res : resources) {
            Long sessionId = res.getSession().getId();
            Date startDate = res.getSession().getStartDate();

            wrapperMap.computeIfAbsent(sessionId, id -> {
                DtoProgressWrapper wrapper = new DtoProgressWrapper();
                wrapper.setSessionId(sessionId);
                wrapper.setStartDate(startDate);
                wrapper.setProgressList(new ArrayList<>());
                return wrapper;
            });

            wrapperMap.get(sessionId).getProgressList().add(sessionMapper.ResourceToProgress(res));
        }

        for (SessionReservation reservation : reservations) {
            Long sessionId = reservation.getSession().getId();
            Date startDate = reservation.getSession().getStartDate();

            wrapperMap.computeIfAbsent(sessionId, id -> {
                DtoProgressWrapper wrapper = new DtoProgressWrapper();
                wrapper.setSessionId(sessionId);
                wrapper.setStartDate(startDate);
                wrapper.setProgressList(new ArrayList<>());
                return wrapper;
            });

            wrapperMap.get(sessionId).getProgressList().add(sessionMapper.ReservationToProgress(reservation));
        }

        ArrayList<DtoProgressWrapper> progressList = new ArrayList<>(wrapperMap.values());
        int score = 0;
        for(DtoProgressWrapper wrapper : progressList)
        {
            for(DtoProgressResponse progress: wrapper.getProgressList())
            {
                score += progress.getGrade();
            }
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setTotalSessionsScore(score);

        return progressList;
    }







    // ============ TUTOR İŞLEMLERİ ==============

    /** Öğretmenin onay bekleyen oturumlarını getirir */
    @Override
    public List<DtoSessionResponse> getPendingSessionsDto(Long tutorId) {
        List<Session> sessions = sessionRepository.findByTutorIdAndStatus(tutorId, SessionStatus.PENDING_APPROVAL);
        return sessions.stream().map(sessionMapper::toResponse).toList();
    }

    /** Belirli bir oturumu onaylar */
    @Override
    public void approveSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        session.setStatus(SessionStatus.CONFIRMED);
        sessionRepository.save(session);

        Tutor tutor = session.getTutor();
        if (tutor != null) {
            tutor.setTotalStudent(tutor.getTotalStudent() + 1);
            tutorRepository.save(tutor);
        }
    }

    /** Belirli bir oturumu iptal eder */
    @Override
    public void cancelSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);

        Tutor tutor = session.getTutor();
        if (tutor != null) {
            tutor.setTotalStudent(tutor.getTotalStudent() - 1);
            tutorRepository.save(tutor);
        }
    }

    /** Öğretmenin onaylanmış oturumlarını getirir */
    @Override
    public List<DtoSessionResponse> getSessionsByTutorId(Long tutorId) {
//        List<Session> sessions = sessionRepository.findByTutorIdAndStatus(tutorId , SessionStatus.CONFIRMED);
        List<Session> sessions = sessionRepository.findByTutorId(tutorId);

        Date now = new Date();

        for (Session session : sessions) {
            if (session.getEndDate().before(now)) {
                session.setStatus(SessionStatus.COMPLETED);
                sessionRepository.save(session);
            }
        }

        return sessions.stream()
                .map(sessionMapper::toResponse)
                .collect(Collectors.toList());
    }



    /** Öğretmenin tüm öğrencilerine ait ilerlemeleri getirir */
    @Override
    public List<DtoProgressWrapper> getSessionProgressByTutorId(Long tutorId) {
        List<Resource> resources = resourceRepository.findResourcesByTutorId(tutorId);
        List<SessionReservation> reservations = reservationRepository.findBySession_Tutor_Id(tutorId);

        Map<Long, DtoProgressWrapper> wrapperMap = new HashMap<>();

        for (Resource res : resources) {
            Long sessionId = res.getSession().getId();
            Date startDate = res.getSession().getStartDate();

            wrapperMap.computeIfAbsent(sessionId, id -> {
                DtoProgressWrapper wrapper = new DtoProgressWrapper();
                wrapper.setSessionId(sessionId);
                wrapper.setStartDate(startDate);
                wrapper.setProgressList(new ArrayList<>());
                return wrapper;
            });

            wrapperMap.get(sessionId).getProgressList().add(sessionMapper.ResourceToProgress(res));
        }

        for (SessionReservation reservation : reservations) {
            Long sessionId = reservation.getSession().getId();
            Date startDate = reservation.getSession().getStartDate();

            wrapperMap.computeIfAbsent(sessionId, id -> {
                DtoProgressWrapper wrapper = new DtoProgressWrapper();
                wrapper.setSessionId(sessionId);
                wrapper.setStartDate(startDate);
                wrapper.setProgressList(new ArrayList<>());
                return wrapper;
            });

            wrapperMap.get(sessionId).getProgressList().add(sessionMapper.ReservationToProgress(reservation));
        }

        return new ArrayList<>(wrapperMap.values());
    }



}

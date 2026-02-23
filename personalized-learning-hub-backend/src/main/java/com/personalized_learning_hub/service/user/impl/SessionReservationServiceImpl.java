package com.personalized_learning_hub.service.user.impl;

import com.personalized_learning_hub.dto.session.DtoSessionReservationRequest;
import com.personalized_learning_hub.dto.session.DtoSessionReservationResponse;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.SessionReservation;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.enums.SessionReservationStatus;
import com.personalized_learning_hub.mapper.SessionReservationMapper;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.repository.SessionReservationRepository;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.repository.TutorRepository;
import com.personalized_learning_hub.service.system.DataTimeUtilService;
import com.personalized_learning_hub.service.user.SessionReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionReservationServiceImpl implements SessionReservationService {

    // === DEPENDENCY ===
    private final SessionReservationRepository reservationRepository;
    private final SessionRepository sessionRepository;
    private final SessionReservationMapper reservationMapper;
    private final DataTimeUtilService dataTimeUtilService;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    // ========== CRUD İŞLEMLERİ ==========

    /** Yeni rezervasyon oluşturur */
    @Override
    public DtoSessionReservationResponse createReservation(DtoSessionReservationRequest request) {
        // Mapper içinde sessionId DTO'dan alınıp session nesnesi oluşturuluyor
        SessionReservation reservation = reservationMapper.toEntity(request);

        // Rezervasyonu kaydet
        SessionReservation saved = reservationRepository.save(reservation);

        // DTO olarak geri döndür
        return reservationMapper.toDto(saved);
    }

    /** Rezervasyonu siler */
    @Override
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    /** Rezervasyonu günceller */

    @Override
    public DtoSessionReservationResponse updateReservation(Long reservationId, DtoSessionReservationRequest request) {
        SessionReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservationMapper.updateEntity(reservation, request);
        SessionReservation updated = reservationRepository.save(reservation);


        if (request.getTutorEvaluationRating() > 0 && reservation.getStatus() == SessionReservationStatus.COMPLETED) {
            // Tutor rating güncellemesi
            Tutor tutor = reservation.getSession().getTutor();
            int newRating = request.getTutorEvaluationRating();
            tutor.setRating(((tutor.getRating() * tutor.getTotalMeeting()) + newRating) / (tutor.getTotalMeeting() + 1));
            tutor.setTotalMeeting(tutor.getTotalMeeting() + 1);
            tutorRepository.save(tutor);

            // Session finalScore güncellemesi
            Session session = reservation.getSession();
            session.setFinalScore(session.getFinalScore()+reservation.getScore());
            session.setTotalMeetings(session.getTotalMeetings()+1);
            session.setTotalPrice(session.getTotalPrice()+session.getHourlyRate());
            sessionRepository.save(session);

            // Student totalSessionsScore güncellemesi
            Student student = reservation.getSession().getStudent();
            student.setTotalSessionsScore(student.getTotalSessionsScore() + reservation.getScore());

            studentRepository.save(student);
        }

        return reservationMapper.toDto(updated);
    }




    // ========== STUDENT İŞLEMLERİ ==========

    /** Öğrencinin tüm rezervasyonlarını getirir (DTO) */
    @Override
    public List<DtoSessionReservationResponse> getReservationsByStudent(Long studentId) {
        List<SessionReservation> reservations = reservationRepository.findBySession_Student_Id(studentId);
        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }


    // ========== TUTOR İŞLEMLERİ ==========

    @Override
    public List<DtoSessionReservationResponse> getReservationsByTutor(Long tutorId) {
        List<SessionReservation> reservations = reservationRepository.findBySession_Tutor_Id(tutorId);
        return reservations.stream().map(reservationMapper::toDto).toList();
    }



}

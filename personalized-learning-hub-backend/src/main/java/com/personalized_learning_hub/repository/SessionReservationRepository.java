package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.SessionReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionReservationRepository extends JpaRepository<SessionReservation , Long> {

    // Long findById(long id);


    List<SessionReservation> findBySession_Student_Id( Long studentId);

    List<SessionReservation> findBySession_Tutor_Id(Long tutorId);


}

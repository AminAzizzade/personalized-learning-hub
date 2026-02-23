package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

//    @Query("SELECT t FROM Tutor t WHERE t.subjectExpertise = :subject " +
//            "AND t.availableTimes LIKE %:timeStr%")
//    List<Tutor> findAvailableTutorsBySubjectAndTime(@Param("subject") String subject,
//                                                    @Param("timeStr") String timeStr);
//


    Optional<Tutor> findByUser(User user);

}

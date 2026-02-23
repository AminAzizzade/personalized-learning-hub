package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByStudentId(Long studentId);

    List<Session> findByTutorIdAndStatus(Long tutorId, SessionStatus status);

    List<Session> findByTutorId(Long tutorId);

    @Query("SELECT DISTINCT s.student FROM Session s WHERE s.tutor.id = :tutorId")
    List<Student> findDistinctStudentsByTutorId(@Param("tutorId") Long tutorId);


}

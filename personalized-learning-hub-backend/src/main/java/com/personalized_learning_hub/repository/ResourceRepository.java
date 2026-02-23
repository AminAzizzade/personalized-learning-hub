package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {


    List<Resource> findBySessionId(@Param("sessionId") Long sessionId);

    @Query("""
                SELECT r FROM Resource r
                JOIN r.session s
                WHERE r.isPublic = true AND s.student.id = :studentId
            """)
    List<Resource> findVisibleResourcesByStudentId(@Param("studentId") Long studentId);

    @Query("""
    SELECT r FROM Resource r
    JOIN r.session s
    WHERE s.student.id = :studentId
""")
    List<Resource> findResourcesByStudentId(@Param("studentId") Long studentId);


    List<Resource> findAllByIsPublicTrue();

    @Query("""
        SELECT r FROM Resource r
        WHERE r.session.tutor.id = :tutorId
    """)
    List<Resource> findByTutorId(@Param("tutorId") Long tutorId);



    @Query("SELECT r FROM Resource r WHERE r.session.tutor.id = :tutorId")
    List<Resource> findResourcesByTutorId(@Param("tutorId") Long tutorId);


}

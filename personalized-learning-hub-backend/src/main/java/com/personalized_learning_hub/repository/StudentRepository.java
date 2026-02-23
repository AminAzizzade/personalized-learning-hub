package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);


}

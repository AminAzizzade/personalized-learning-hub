package com.personalized_learning_hub.repository;

import com.personalized_learning_hub.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

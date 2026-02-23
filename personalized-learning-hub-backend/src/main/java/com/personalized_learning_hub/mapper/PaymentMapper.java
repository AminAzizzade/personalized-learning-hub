package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.system.DtoPaymentResponse;
import com.personalized_learning_hub.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    public DtoPaymentResponse toResponse(Payment payment) {

        return new DtoPaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentStatus(),
                payment.getSessionReservation().getId()
        );
    }
}


package com.personalized_learning_hub.dto.system;

import com.personalized_learning_hub.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class DtoPaymentResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
    private Long sessionReservationId;
}


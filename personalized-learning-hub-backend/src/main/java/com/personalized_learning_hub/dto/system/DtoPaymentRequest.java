package com.personalized_learning_hub.dto.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoPaymentRequest {
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private Long sessionId;
}


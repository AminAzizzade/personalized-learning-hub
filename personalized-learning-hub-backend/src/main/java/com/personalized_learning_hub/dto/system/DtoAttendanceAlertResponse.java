package com.personalized_learning_hub.dto.system;


import com.personalized_learning_hub.enums.AlertType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DtoAttendanceAlertResponse {
    private Long id;
    private AlertType alertType;
    private String description;
    private LocalDateTime createdAt;
    private Long sessionId;
}




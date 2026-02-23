package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.system.DtoAttendanceAlertResponse;
import com.personalized_learning_hub.entity.AttendanceAlert;
import org.springframework.stereotype.Component;

@Component
public class AttendanceAlertMapper {

    public DtoAttendanceAlertResponse toDto(AttendanceAlert alert) {
        DtoAttendanceAlertResponse dto = new DtoAttendanceAlertResponse();
        dto.setId(alert.getId());
        dto.setAlertType(alert.getAlertType());
        dto.setDescription(alert.getDescription());
        dto.setCreatedAt(alert.getCreatedAt());
        dto.setSessionId(alert.getSession().getId());
        return dto;
    }


}



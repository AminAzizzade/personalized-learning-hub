package com.personalized_learning_hub.dto.resource;

import com.personalized_learning_hub.enums.ResourceStatus;
import com.personalized_learning_hub.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoResourceResponse {

    private Long id;
    private String resourceName;
    private String description;
    private String category;
    private boolean isPublic;

    private String teacherFileName;
    private String studentFileName;

    private ResourceType type;
    private ResourceStatus status;

    private int homeWorkScore;
    private Date deadLine;
    private LocalDateTime createdAt;

    private Long sessionId;



}

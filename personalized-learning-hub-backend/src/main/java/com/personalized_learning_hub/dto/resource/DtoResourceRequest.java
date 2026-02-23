package com.personalized_learning_hub.dto.resource;

import com.personalized_learning_hub.enums.ResourceType;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoResourceRequest {

    private String resourceName;
    private String description;
    private String category;
    private boolean isPublic;

    private ResourceType type; // ASSIGNMENT, MATERIAL
    private int homeWorkScore;
    private Date deadLine;

    private Long sessionId;

}

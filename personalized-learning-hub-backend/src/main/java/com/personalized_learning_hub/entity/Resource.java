package com.personalized_learning_hub.entity;

import com.personalized_learning_hub.enums.ResourceStatus;
import com.personalized_learning_hub.enums.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceName = "";
    private String description = "";
    private String category = ""; // örn: PDF, Video
    private boolean isPublic = false;

    private String teacherFileName = "";
    private String studentFileName = "";

    @Enumerated(EnumType.STRING)
    private ResourceType type = ResourceType.MATERIAL;

    @Column(nullable = false)
    private int homeWorkScore = 0;

    private Date deadLine;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status = ResourceStatus.PENDING_SUBMISSION;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;



}

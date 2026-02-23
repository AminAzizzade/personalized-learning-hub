package com.personalized_learning_hub.mapper;

import com.personalized_learning_hub.dto.resource.DtoResourceRequest;
import com.personalized_learning_hub.dto.resource.DtoResourceResponse;
import com.personalized_learning_hub.dto.resource.DtoUploadHomeworkRequest;
import com.personalized_learning_hub.entity.Resource;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.enums.ResourceStatus;
import com.personalized_learning_hub.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Component
public class ResourceMapper {

    private final SessionRepository sessionRepository;

    // DTO -> Entity
    public Resource toEntity(DtoResourceRequest dto, String fileName) {
        Resource r = new Resource();
        r.setResourceName(dto.getResourceName());
        r.setDescription(dto.getDescription());
        r.setCategory(dto.getCategory());
        r.setPublic(dto.isPublic());
        r.setType(dto.getType());
        r.setHomeWorkScore(dto.getHomeWorkScore());
        r.setDeadLine(dto.getDeadLine());

        r.setTeacherFileName(fileName);
        r.setCreatedAt(LocalDateTime.now());
        r.setStatus(ResourceStatus.PENDING_SUBMISSION);

        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        r.setSession(session);

        return r;
    }

    // Entity -> DTO
    public DtoResourceResponse toDto(Resource r) {
        DtoResourceResponse dto = new DtoResourceResponse();
        dto.setId(r.getId());
        dto.setResourceName(r.getResourceName());
        dto.setDescription(r.getDescription());
        dto.setCategory(r.getCategory());
        dto.setPublic(r.isPublic());
        dto.setTeacherFileName(r.getTeacherFileName());
        dto.setStudentFileName(r.getStudentFileName());
        dto.setType(r.getType());
        dto.setStatus(r.getStatus());
        dto.setHomeWorkScore(r.getHomeWorkScore());
        dto.setDeadLine(r.getDeadLine());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setSessionId(r.getSession().getId());
        return dto;
    }


    public void uploadHomework(DtoUploadHomeworkRequest request)
    {

    }

    // Update mevcut Resource
    public void updateEntity(Resource existing, DtoResourceRequest dto) {
        existing.setResourceName(dto.getResourceName());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setPublic(dto.isPublic());
        existing.setType(dto.getType());
        existing.setHomeWorkScore(dto.getHomeWorkScore());
        existing.setDeadLine(dto.getDeadLine());

        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        existing.setSession(session);
    }
}
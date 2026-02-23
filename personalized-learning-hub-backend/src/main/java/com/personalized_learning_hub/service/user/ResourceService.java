package com.personalized_learning_hub.service.user;

import com.personalized_learning_hub.dto.resource.DtoResourceRequest;
import com.personalized_learning_hub.dto.resource.DtoResourceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ResourceService {



    // (1) CRUD işlemleri
    void delete(Long id);
    DtoResourceResponse updateResource(Long id, DtoResourceRequest dto);
    DtoResourceResponse createResourceWithFile(DtoResourceRequest dto, MultipartFile file);



    // === STUDENT İŞLEMLERİ ===
    List<DtoResourceResponse> getVisibleResources(Long studentId);
    boolean handleHomeworkUpload(Long resourceId, MultipartFile file);
    ResponseEntity<org.springframework.core.io.Resource> downloadFile(String filename);


    // === TUTOR İŞLEMLERİ ===

    Map<Long, List<DtoResourceResponse>> getGroupedResourcesByStudent(Long tutorId);
    void gradeAssignment(Long resourceId, int grade);


}

package com.personalized_learning_hub.controller.user;

import com.personalized_learning_hub.dto.resource.DtoResourceRequest;
import com.personalized_learning_hub.dto.resource.DtoResourceResponse;
import com.personalized_learning_hub.service.user.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;


    // ========== CRUD İŞLEMLERİ ==========

    /** Yeni kaynak oluştur (dosya ile birlikte) */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DtoResourceResponse> createResourceWithFile(
            @RequestPart("dto") DtoResourceRequest dto,
            @RequestPart("file") MultipartFile file) {
        DtoResourceResponse response = resourceService.createResourceWithFile(dto, file);
        return ResponseEntity.ok(response);
    }

    /**  Kaynağı güncelle */
//    @PutMapping("/{id}")
//    public ResponseEntity<DtoResourceResponse> updateResource(
//            @PathVariable Long id,
//            @RequestBody DtoResourceRequest dto) {
//        DtoResourceResponse updated = resourceService.updateResource(id, dto);
//        return ResponseEntity.ok(updated);
//    }
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<DtoResourceResponse> updateResource(
            @PathVariable Long id,
            @RequestBody DtoResourceRequest dto) {
        return ResponseEntity.ok(resourceService.updateResource(id, dto));
    }

    /**  Kaynağı sil */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
//        resourceService.delete(id);
//        return ResponseEntity.noContent().build();
//    }



    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }




    // ============= STUDENT İŞLEMLERİ =============

    /** Öğrenciye atanmış kaynakları getirir */
    @GetMapping("/student/{studentId}")
    public List<DtoResourceResponse> getResourcesVisibleToStudent(@PathVariable Long studentId) {
        return resourceService.getVisibleResources(studentId);
    }

    /** Öğrencinin ödev dosyasını yüklemesini sağlar */
    @PostMapping(value = "/{resourceId}/solution", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean uploadHomework(@PathVariable Long resourceId, @RequestPart("file") MultipartFile file) {
        return resourceService.handleHomeworkUpload(resourceId, file);
    }

    /** Öğrencinin dosya indirmesini sağlar */
    @GetMapping("/download")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@RequestParam("fileName") String filename) {
        return resourceService.downloadFile(filename);
    }



    // ============ TUTOR İŞLEMLERİ =============

    /** Öğretmenin kaynaklarını öğrencilere göre gruplu şekilde getirir */
    @GetMapping("/tutor/{tutorId}/grouped-by-student")
    public ResponseEntity<Map<Long, List<DtoResourceResponse>>> getGroupedResources(@PathVariable Long tutorId) {
        return ResponseEntity.ok(resourceService.getGroupedResourcesByStudent(tutorId));
    }


    /** Belirli bir kaynağa (ödev) not verir */
    @PutMapping("/{resourceId}/grade")
    public void gradeAssignment(@PathVariable Long resourceId, @RequestBody DtoResourceRequest dto) {
        int grade = dto.getHomeWorkScore();  // doğrudan DTO'dan alıyoruz
        resourceService.gradeAssignment(resourceId, grade);
    }
}

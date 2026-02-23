package com.personalized_learning_hub.service.user.impl;

import com.personalized_learning_hub.dto.resource.DtoResourceRequest;
import com.personalized_learning_hub.dto.resource.DtoResourceResponse;
import com.personalized_learning_hub.entity.Resource;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.enums.ResourceStatus;
import com.personalized_learning_hub.mapper.ResourceMapper;
import com.personalized_learning_hub.repository.ResourceRepository;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.service.system.FileStorageService;
import com.personalized_learning_hub.service.user.ResourceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Service
@RequiredArgsConstructor
@Slf4j

public class ResourceServiceImpl implements ResourceService {

    // === DEPENDENCY ===
    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final FileStorageService fileStorageService;
    private final SessionRepository sessionRepository;
    private final StudentRepository studentRepository;

    // === CRUD İŞLEMLERİ ===

    /** Dosya yükleme + resource oluşturma */
    @Override
    public DtoResourceResponse createResourceWithFile(DtoResourceRequest dto, MultipartFile file) {
        try {
            fileStorageService.saveFile(file);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            Resource entity = resourceMapper.toEntity(dto, fileName);
            Session session = sessionRepository.getReferenceById(dto.getSessionId());
            entity.setSession(session);

            Resource saved = resourceRepository.save(entity);
            return resourceMapper.toDto(saved);
        } catch (IOException e) {
            throw new RuntimeException("Dosya yükleme sırasında hata oluştu", e);
        }
    }

    /** ID'ye göre kaynağı siler */
    @Override
    public void delete(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new EntityNotFoundException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }

    /** Kaynağı günceller */
    @Override
    public DtoResourceResponse updateResource(Long id, DtoResourceRequest dto) {
        Resource existing = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));

        resourceMapper.updateEntity(existing, dto);
        Resource updated = resourceRepository.save(existing);
        return resourceMapper.toDto(updated);
    }




    // === TUTOR İŞLEMLERİ ===

    /** Öğretmenin kaynaklarını öğrencilere göre gruplu şekilde getirir */
    @Override
    public Map<Long, List<DtoResourceResponse>> getGroupedResourcesByStudent(Long tutorId) {
        List<Resource> resources = resourceRepository.findByTutorId(tutorId);

        Map<Long, List<DtoResourceResponse>> groupedDtoMap = new HashMap<>();
        for (Resource resource : resources) {
            Long studentId = resource.getSession().getStudent().getId();
            groupedDtoMap
                    .computeIfAbsent(studentId, k -> new ArrayList<>())
                    .add(resourceMapper.toDto(resource));
        }

        return groupedDtoMap;
    }

    /** Belirli bir kaynağa (ödev) not verir */
    @Override
    public void gradeAssignment(Long resourceId, int grade) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setHomeWorkScore(grade);
        resource.setStatus(ResourceStatus.COMPLETED);
        resourceRepository.save(resource);

        // Session finalScore güncellemesi
        Session session = resource.getSession();
        session.setFinalScore(session.getFinalScore()+resource.getHomeWorkScore());
        sessionRepository.save(session);

        // Student totalSessionsScore güncellemesi
        Student student = resource.getSession().getStudent();
        student.setTotalSessionsScore(student.getTotalSessionsScore() + resource.getHomeWorkScore());
        studentRepository.save(student);
    }



    // === STUDENT İŞLEMLERİ ===

    /** Öğrenciye gösterilecek kaynakları DTO olarak döner */
    @Override
    public List<DtoResourceResponse> getVisibleResources(Long studentId) {
        List<Resource> resources = resourceRepository.findResourcesByStudentId(studentId);
        return resources.stream()
                .map(resourceMapper::toDto)
                .toList();
    }

    /** Ödev yüklemesini gerçekleştirir ve durumu günceller */
    @Override
    public boolean handleHomeworkUpload(Long resourceId, MultipartFile file) {
        try {
            fileStorageService.saveFile(file);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            uploadHomework(resourceId, fileName);
            return true;
        } catch (IOException e) {
            log.error("Dosya yüklenirken hata", e, Level.SEVERE);
            return false;
        }
    }

    /** Ödev dosyasının ismini kaydeder ve durumu günceller */
    public void uploadHomework(Long resourceId, String fileName) {
        Resource existing = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource bulunamadı"));

        existing.setStudentFileName(fileName);
        existing.setStatus(ResourceStatus.REVIEWED);

        resourceRepository.save(existing);
    }

    /** Dosya indirme işlemini gerçekleştirir */
    @Override
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(String filename) {
        try {
            var fileToDownload = fileStorageService.getDownloadFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}

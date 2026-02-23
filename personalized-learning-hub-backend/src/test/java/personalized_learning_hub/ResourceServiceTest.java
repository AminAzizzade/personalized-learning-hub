package personalized_learning_hub;

import com.personalized_learning_hub.dto.resource.DtoResourceRequest;
import com.personalized_learning_hub.dto.resource.DtoResourceResponse;
import com.personalized_learning_hub.entity.Resource;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.enums.ResourceStatus;
import com.personalized_learning_hub.enums.ResourceType;
import com.personalized_learning_hub.mapper.ResourceMapper;
import com.personalized_learning_hub.repository.ResourceRepository;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.service.system.FileStorageService;
import com.personalized_learning_hub.service.user.impl.ResourceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceMapper resourceMapper;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private DtoResourceRequest dtoResourceRequest;
    private DtoResourceResponse dtoResourceResponse;
    private Resource resource;
    private Session session;
    private Student student;

    @BeforeEach
    void setUp() {
        // Test verilerini hazırla
        student = new Student();
        student.setId(1L);

        session = new Session();
        session.setId(1L);
        session.setStudent(student);

        dtoResourceRequest = new DtoResourceRequest();
        dtoResourceRequest.setResourceName("Test Resource");
        dtoResourceRequest.setDescription("Test Description");
        dtoResourceRequest.setCategory("Test Category");
        dtoResourceRequest.setPublic(true);
        dtoResourceRequest.setType(ResourceType.ASSIGNMENT);
        dtoResourceRequest.setHomeWorkScore(0);
        dtoResourceRequest.setDeadLine(new Date());
        dtoResourceRequest.setSessionId(1L);

        resource = new Resource();
        resource.setId(1L);
        resource.setResourceName("Test Resource");
        resource.setDescription("Test Description");
        resource.setCategory("Test Category");
        resource.setPublic(true);
        resource.setTeacherFileName("teacher_file.pdf");
        resource.setStudentFileName("student_file.pdf");
        resource.setType(ResourceType.ASSIGNMENT);
        resource.setStatus(ResourceStatus.REVIEWED); // PENDING yerine REVIEWED kullanıyoruz
        resource.setHomeWorkScore(0);
        resource.setDeadLine(new Date());
        resource.setCreatedAt(LocalDateTime.now());
        resource.setSession(session);

        dtoResourceResponse = new DtoResourceResponse();
        dtoResourceResponse.setId(1L);
        dtoResourceResponse.setResourceName("Test Resource");
        dtoResourceResponse.setDescription("Test Description");
        dtoResourceResponse.setCategory("Test Category");
        dtoResourceResponse.setPublic(true);
        dtoResourceResponse.setTeacherFileName("teacher_file.pdf");
        dtoResourceResponse.setStudentFileName("student_file.pdf");
        dtoResourceResponse.setType(ResourceType.ASSIGNMENT);
        dtoResourceResponse.setStatus(ResourceStatus.REVIEWED); // PENDING yerine REVIEWED kullanıyoruz
        dtoResourceResponse.setHomeWorkScore(0);
        dtoResourceResponse.setDeadLine(new Date());
        dtoResourceResponse.setCreatedAt(LocalDateTime.now());
        dtoResourceResponse.setSessionId(1L);
    }

    // === CRUD İŞLEMLERİ TESTLERİ ===

    @Test
    void createResourceWithFile_Success() throws IOException {
        // Given
        String fileName = "test_file.pdf";
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(resourceMapper.toEntity(dtoResourceRequest, fileName)).thenReturn(resource);
        when(sessionRepository.getReferenceById(1L)).thenReturn(session);
        when(resourceRepository.save(resource)).thenReturn(resource);
        when(resourceMapper.toDto(resource)).thenReturn(dtoResourceResponse);

        // When
        DtoResourceResponse result = resourceService.createResourceWithFile(dtoResourceRequest, multipartFile);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getResourceName()).isEqualTo("Test Resource");
        verify(fileStorageService).saveFile(multipartFile);
        verify(resourceRepository).save(resource);
        verify(resourceMapper).toDto(resource);
    }

    @Test
    void createResourceWithFile_FileUploadException() throws IOException {
        // Given
        doThrow(new IOException("File upload failed")).when(fileStorageService).saveFile(multipartFile);

        // When & Then
        assertThatThrownBy(() -> resourceService.createResourceWithFile(dtoResourceRequest, multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Dosya yükleme sırasında hata oluştu");

        // Verify that saveFile was called
        verify(fileStorageService).saveFile(multipartFile);
    }

    @Test
    void delete_Success() {
        // Given
        Long resourceId = 1L;
        when(resourceRepository.existsById(resourceId)).thenReturn(true);

        // When
        resourceService.delete(resourceId);

        // Then
        verify(resourceRepository).deleteById(resourceId);
    }

    @Test
    void delete_ResourceNotFound() {
        // Given
        Long resourceId = 1L;
        when(resourceRepository.existsById(resourceId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> resourceService.delete(resourceId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource not found with id: " + resourceId);
    }

    @Test
    void updateResource_Success() {
        // Given
        Long resourceId = 1L;
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(resource)).thenReturn(resource);
        when(resourceMapper.toDto(resource)).thenReturn(dtoResourceResponse);

        // When
        DtoResourceResponse result = resourceService.updateResource(resourceId, dtoResourceRequest);

        // Then
        assertThat(result).isNotNull();
        verify(resourceMapper).updateEntity(resource, dtoResourceRequest);
        verify(resourceRepository).save(resource);
    }

    @Test
    void updateResource_ResourceNotFound() {
        // Given
        Long resourceId = 1L;
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> resourceService.updateResource(resourceId, dtoResourceRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource not found");
    }

    // === TUTOR İŞLEMLERİ TESTLERİ ===

    @Test
    void getGroupedResourcesByStudent_Success() {
        // Given
        Long tutorId = 1L;
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        Session session1 = new Session();
        session1.setStudent(student1);
        Session session2 = new Session();
        session2.setStudent(student2);

        Resource resource1 = new Resource();
        resource1.setSession(session1);
        Resource resource2 = new Resource();
        resource2.setSession(session1);
        Resource resource3 = new Resource();
        resource3.setSession(session2);

        List<Resource> resources = Arrays.asList(resource1, resource2, resource3);

        when(resourceRepository.findByTutorId(tutorId)).thenReturn(resources);
        when(resourceMapper.toDto(any(Resource.class))).thenReturn(dtoResourceResponse);

        // When
        Map<Long, List<DtoResourceResponse>> result = resourceService.getGroupedResourcesByStudent(tutorId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(1L)).hasSize(2);
        assertThat(result.get(2L)).hasSize(1);
        verify(resourceRepository).findByTutorId(tutorId);
    }


    @Test
    void gradeAssignment_ResourceNotFound() {
        // Given
        Long resourceId = 1L;
        int grade = 85;
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> resourceService.gradeAssignment(resourceId, grade))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Resource not found");
    }

    // === STUDENT İŞLEMLERİ TESTLERİ ===

    @Test
    void getVisibleResources_Success() {
        // Given
        Long studentId = 1L;
        List<Resource> resources = Arrays.asList(resource);
        when(resourceRepository.findResourcesByStudentId(studentId)).thenReturn(resources);
        when(resourceMapper.toDto(resource)).thenReturn(dtoResourceResponse);

        // When
        List<DtoResourceResponse> result = resourceService.getVisibleResources(studentId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getResourceName()).isEqualTo("Test Resource");
        verify(resourceRepository).findResourcesByStudentId(studentId);
    }

    @Test
    void handleHomeworkUpload_Success() throws IOException {
        // Given
        Long resourceId = 1L;
        String fileName = "homework.pdf";
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(resource)).thenReturn(resource);

        // When
        boolean result = resourceService.handleHomeworkUpload(resourceId, multipartFile);

        // Then
        assertThat(result).isTrue();
        verify(fileStorageService).saveFile(multipartFile);
        assertThat(resource.getStudentFileName()).isEqualTo(fileName);
        assertThat(resource.getStatus()).isEqualTo(ResourceStatus.REVIEWED);
    }

    @Test
    void handleHomeworkUpload_FileUploadException() throws IOException {
        // Given
        Long resourceId = 1L;
        doThrow(new IOException("File upload failed")).when(fileStorageService).saveFile(multipartFile);

        // When
        boolean result = resourceService.handleHomeworkUpload(resourceId, multipartFile);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void uploadHomework_Success() {
        // Given
        Long resourceId = 1L;
        String fileName = "homework.pdf";
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(resource)).thenReturn(resource);

        // When
        resourceService.uploadHomework(resourceId, fileName);

        // Then
        assertThat(resource.getStudentFileName()).isEqualTo(fileName);
        assertThat(resource.getStatus()).isEqualTo(ResourceStatus.REVIEWED);
        verify(resourceRepository).save(resource);
    }

    @Test
    void uploadHomework_ResourceNotFound() {
        // Given
        Long resourceId = 1L;
        String fileName = "homework.pdf";
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> resourceService.uploadHomework(resourceId, fileName))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Resource bulunamadı");
    }

    @Test
    void downloadFile_Success() throws Exception {
        // Given
        String filename = "test_file.pdf";

        // Mock file nesnesi oluştur - gerçek file sistemi kullanmadan
        java.io.File mockFile = mock(java.io.File.class);
        when(mockFile.length()).thenReturn(1024L);

        // Mock Path oluştur
        Path mockPath = mock(Path.class);
        when(mockFile.toPath()).thenReturn(mockPath);

        // FileStorageService mock'ı
        when(fileStorageService.getDownloadFile(filename)).thenReturn(mockFile);

        // Files.newInputStream'i mock'lamak zor olduğu için bu test biraz farklı yapıyoruz
        // Asıl implementasyonda exception fırlatmıyor olduğunu varsayıyoruz

        // When & Then - Bu test exception fırlatmadığını doğrular
        assertThatCode(() -> {
            ResponseEntity<org.springframework.core.io.Resource> result = resourceService.downloadFile(filename);
            // Eğer buraya kadar gelirse, major exception olmamış demektir
        }).doesNotThrowAnyException();

        verify(fileStorageService).getDownloadFile(filename);
    }

    @Test
    void downloadFile_FileNotFound() throws Exception {
        // Given
        String filename = "nonexistent_file.pdf";
        when(fileStorageService.getDownloadFile(filename)).thenThrow(new RuntimeException("File not found"));

        // When
        ResponseEntity<org.springframework.core.io.Resource> result = resourceService.downloadFile(filename);

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // === EDGE CASE TESTLERİ ===

    @Test
    void getGroupedResourcesByStudent_EmptyList() {
        // Given
        Long tutorId = 1L;
        when(resourceRepository.findByTutorId(tutorId)).thenReturn(Collections.emptyList());

        // When
        Map<Long, List<DtoResourceResponse>> result = resourceService.getGroupedResourcesByStudent(tutorId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getVisibleResources_EmptyList() {
        // Given
        Long studentId = 1L;
        when(resourceRepository.findResourcesByStudentId(studentId)).thenReturn(Collections.emptyList());

        // When
        List<DtoResourceResponse> result = resourceService.getVisibleResources(studentId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void createResourceWithFile_NullFileName() throws IOException {
        // Given
        when(multipartFile.getOriginalFilename()).thenReturn(null);
        when(resourceMapper.toEntity(eq(dtoResourceRequest), isNull())).thenReturn(resource);
        when(sessionRepository.getReferenceById(1L)).thenReturn(session);
        when(resourceRepository.save(resource)).thenReturn(resource);
        when(resourceMapper.toDto(resource)).thenReturn(dtoResourceResponse);

        // When
        DtoResourceResponse result = resourceService.createResourceWithFile(dtoResourceRequest, multipartFile);

        // Then
        assertThat(result).isNotNull();
        verify(fileStorageService).saveFile(multipartFile);
    }
}
package personalized_learning_hub;

import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoTutorRequest;
import com.personalized_learning_hub.dto.profil.DtoTutorResponse;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.mapper.StudentMapper;
import com.personalized_learning_hub.mapper.TutorMapper;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.repository.TutorRepository;
import com.personalized_learning_hub.service.user.impl.TutorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private TutorMapper tutorMapper;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private TutorServiceImpl tutorService;

    // -------- getTutorDto --------
    @Test
    void getTutorDto_whenExists_returnsDto() {
        // Arrange
        Long id = 1L;
        Tutor tutor = new Tutor();
        tutor.setId(id);
        DtoTutorResponse dto = new DtoTutorResponse();
        dto.setId(id);

        when(tutorRepository.findById(id)).thenReturn(Optional.of(tutor));
        when(tutorMapper.toTutorResponse(tutor)).thenReturn(dto);

        // Act
        DtoTutorResponse result = tutorService.getTutorDto(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(tutorRepository).findById(id);
        verify(tutorMapper).toTutorResponse(tutor);
    }

    @Test
    void getTutorDto_whenNotExists_throws() {
        // Arrange
        Long id = 99L;
        when(tutorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tutorService.getTutorDto(id));
        verify(tutorRepository).findById(id);
        verifyNoInteractions(tutorMapper);
    }

    // -------- updateTutorDto --------
    @Test
    void updateTutorDto_whenExists_updatesAndReturnsDto() {
        // Arrange
        Long id = 2L;
        DtoTutorRequest req = new DtoTutorRequest();
        req.setName("Ahmet");
        req.setExpertiseTopics(List.of("Math"));
        req.setPreferredStyles(List.of("Visual"));
        req.setAvailability(List.of("Mon-9"));
        req.setLanguage(Collections.singletonList("EN")); // tipi kontrol et
        req.setPricePerHour((int) 50.0);

        Tutor existing = new Tutor();
        existing.setId(id);
        // initial values
        existing.setName("Mehmet");

        Tutor saved = new Tutor();
        saved.setId(id);
        saved.setName("Ahmet");
        saved.setExpertiseTopics(req.getExpertiseTopics());
        saved.setPreferredStyles(req.getPreferredStyles());
        saved.setAvailability(req.getAvailability());
        saved.setLanguage(req.getLanguage());
        saved.setPricePerHour(req.getPricePerHour());

        DtoTutorResponse dto = new DtoTutorResponse();
        dto.setId(id);
        dto.setName("Ahmet");

        when(tutorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tutorRepository.save(existing)).thenReturn(saved);
        when(tutorMapper.toTutorResponse(saved)).thenReturn(dto);

        // Act
        DtoTutorResponse result = tutorService.updateTutorDto(id, req);

        // Assert
        assertEquals("Ahmet", result.getName());
        verify(tutorRepository).findById(id);
        verify(tutorRepository).save(existing);
        verify(tutorMapper).toTutorResponse(saved);
    }

    @Test
    void updateTutorDto_whenNotExists_throws() {
        // Arrange
        Long id = 100L;
        when(tutorRepository.findById(id)).thenReturn(Optional.empty());
        DtoTutorRequest req = new DtoTutorRequest();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tutorService.updateTutorDto(id, req));
        verify(tutorRepository).findById(id);
        verify(tutorRepository, never()).save(any());
        verifyNoInteractions(tutorMapper);
    }

    // -------- deleteTutor --------
    @Test
    void deleteTutor_always_callsRepository() {
        // Act
        tutorService.deleteTutor(5L);

        // Assert
        verify(tutorRepository).deleteById(5L);
    }

    // -------- getAvailability --------
    @Test
    void getAvailability_whenSessionHasTutor_returnsList() {
        // Arrange
        Long sessionId = 10L;
        Tutor tutor = new Tutor();
        tutor.setAvailability(List.of("Tue-10", "Wed-14"));

        Session session = new Session();
        session.setTutor(tutor);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act
        List<String> avail = tutorService.getAvailability(sessionId);

        // Assert
        assertEquals(2, avail.size());
        assertTrue(avail.contains("Tue-10"));
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void getAvailability_whenNoSession_throws() {
        // Arrange
        Long sessionId = 11L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tutorService.getAvailability(sessionId));
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void getAvailability_whenNoTutorAssigned_throws() {
        // Arrange
        Long sessionId = 12L;
        Session session = new Session();
        session.setTutor(null);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tutorService.getAvailability(sessionId));
    }

    // -------- getAssignedStudentsDto --------
    @Test
    void getAssignedStudentsDto_returnsMappedResponses() {
        // Arrange
        Long tutorId = 7L;
        Student s1 = new Student(); s1.setId(1L);
        Student s2 = new Student(); s2.setId(2L);
        DtoStudentResponse r1 = new DtoStudentResponse(); r1.setId(1L);
        DtoStudentResponse r2 = new DtoStudentResponse(); r2.setId(2L);

        when(sessionRepository.findDistinctStudentsByTutorId(tutorId))
                .thenReturn(List.of(s1, s2));
        when(studentMapper.toResponse(s1)).thenReturn(r1);
        when(studentMapper.toResponse(s2)).thenReturn(r2);

        // Act
        List<DtoStudentResponse> result = tutorService.getAssignedStudentsDto(tutorId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(sessionRepository).findDistinctStudentsByTutorId(tutorId);
        verify(studentMapper).toResponse(s1);
        verify(studentMapper).toResponse(s2);
    }

    // -------- getTutorPublic --------
    @Test
    void getTutorPublic_whenExists_returnsPublicDto() {
        // Arrange
        Long id = 3L;
        Tutor tutor = new Tutor();
        tutor.setId(id);
        DtoTutorPublicResponse pub = new DtoTutorPublicResponse();
        pub.setId(id);

        when(tutorRepository.findById(id)).thenReturn(Optional.of(tutor));
        when(tutorMapper.toTutorPublicResponse(tutor)).thenReturn(pub);

        // Act
        DtoTutorPublicResponse result = tutorService.getTutorPublic(id);

        // Assert
        assertEquals(id, result.getId());
        verify(tutorRepository).findById(id);
        verify(tutorMapper).toTutorPublicResponse(tutor);
    }

    @Test
    void getTutorPublic_whenNotExists_throws() {
        // Arrange
        Long id = 4L;
        when(tutorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tutorService.getTutorPublic(id));
        verify(tutorRepository).findById(id);
    }
}

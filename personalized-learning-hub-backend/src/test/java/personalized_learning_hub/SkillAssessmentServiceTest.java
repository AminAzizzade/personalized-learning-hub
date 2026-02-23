package personalized_learning_hub;

import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentRequest;
import com.personalized_learning_hub.dto.skillAssessment.DtoSkillAssessmentResponse;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.SkillAssessment;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.mapper.SessionMapper;
import com.personalized_learning_hub.mapper.SkillAssessmentMapper;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.repository.SkillAssessmentRepository;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.service.system.TutorAssignmentService;
import com.personalized_learning_hub.service.user.impl.SkillAssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillAssessmentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SkillAssessmentRepository skillAssessmentRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private TutorAssignmentService tutorAssignmentService;
    @Mock
    private SkillAssessmentMapper skillAssessmentMapper;
    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SkillAssessmentServiceImpl service;

    private DtoSkillAssessmentRequest dtoRequest;
    private Student student;
    private SkillAssessment entity;
    private Tutor tutor;
    private Session sessionObj;

    @BeforeEach
    void setUp() {
        dtoRequest = new DtoSkillAssessmentRequest();
        dtoRequest.setTopic("Java Basics");
        dtoRequest.setLearningGoal("Understand OOP");
        dtoRequest.setPreferredStyle("Visual");
        dtoRequest.setLanguage("English");
        dtoRequest.setPriceRange("Medium");
        dtoRequest.setTotalDuration("2h");
        dtoRequest.setPreferredTutorRating(4);
        dtoRequest.setAvailability(Arrays.asList("Mon", "Wed"));

        student = new Student();
        student.setId(1L);

        entity = new SkillAssessment();
        entity.setId(100L);
        entity.setTopic(dtoRequest.getTopic());
        entity.setLearningGoal(dtoRequest.getLearningGoal());
        entity.setPreferredStyle(dtoRequest.getPreferredStyle());
        entity.setLanguage(dtoRequest.getLanguage());
        entity.setPriceRange(dtoRequest.getPriceRange());
        entity.setTotalDuration(dtoRequest.getTotalDuration());
        entity.setPreferredTutorRating(dtoRequest.getPreferredTutorRating());
        entity.setAvailability(dtoRequest.getAvailability());
        entity.setStudent(student);

        tutor = new Tutor();
        tutor.setId(2L);

        sessionObj = new Session();
        sessionObj.setId(200L);
    }

    @Test
    void createSkillAssessment_whenStudentExists_shouldSaveAndReturnDto() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(skillAssessmentMapper.toEntity(dtoRequest, student)).thenReturn(entity);
        when(skillAssessmentRepository.save(entity)).thenReturn(entity);
        when(tutorAssignmentService.assignBestTutorForSkillAssessment(entity)).thenReturn(tutor);
        when(sessionMapper.fromSkillAssessment(entity, tutor, student)).thenReturn(sessionObj);
        DtoSkillAssessmentResponse expected = buildResponse();
        when(skillAssessmentMapper.toResponse(entity)).thenReturn(expected);

        DtoSkillAssessmentResponse actual = service.createSkillAssessment(dtoRequest, 1L);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTopic(), actual.getTopic());
        assertEquals(expected.getLearningGoal(), actual.getLearningGoal());
        assertEquals(expected.getPreferredStyle(), actual.getPreferredStyle());
        assertEquals(expected.getAvailability(), actual.getAvailability());

        InOrder inOrder = inOrder(studentRepository, skillAssessmentRepository,
                tutorAssignmentService, sessionMapper, sessionRepository, skillAssessmentMapper);
        inOrder.verify(studentRepository).findById(1L);
        inOrder.verify(skillAssessmentRepository).save(entity);
        inOrder.verify(tutorAssignmentService).assignBestTutorForSkillAssessment(entity);
        inOrder.verify(sessionMapper).fromSkillAssessment(entity, tutor, student);
        inOrder.verify(sessionRepository).save(sessionObj);
        inOrder.verify(skillAssessmentMapper).toResponse(entity);
    }

    @Test
    void createSkillAssessment_whenStudentNotFound_shouldThrow() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createSkillAssessment(dtoRequest, 1L));
        assertEquals("Öğrenci bulunamadı", ex.getMessage());
        verifyNoInteractions(skillAssessmentRepository);
    }

    @Test
    void getSkillAssessmentsByStudentId_whenExists_shouldReturnList() {
        when(skillAssessmentRepository.findByStudentId(1L)).thenReturn(List.of(entity));
        DtoSkillAssessmentResponse expected = buildResponse();
        when(skillAssessmentMapper.toResponse(entity)).thenReturn(expected);

        List<DtoSkillAssessmentResponse> list = service.getSkillAssessmentsByStudentId(1L);

        assertEquals(1, list.size());
        assertEquals(expected.getTopic(), list.get(0).getTopic());
    }

    @Test
    void deleteSkillAssessment_shouldInvokeRepository() {
        service.delete(100L);
        verify(skillAssessmentRepository).deleteById(100L);
    }

    private DtoSkillAssessmentResponse buildResponse() {
        return new DtoSkillAssessmentResponse(
                100L,
                "Java Basics",
                "Understand OOP",
                "Visual",
                "English",
                "Medium",
                "2h",
                4,
                Arrays.asList("Mon", "Wed")
        );
    }
}

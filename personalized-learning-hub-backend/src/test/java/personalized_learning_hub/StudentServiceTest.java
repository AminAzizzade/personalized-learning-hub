package personalized_learning_hub;

import com.personalized_learning_hub.dto.profil.DtoStudentPublicResponse;
import com.personalized_learning_hub.dto.profil.DtoStudentRequest;
import com.personalized_learning_hub.dto.profil.DtoStudentResponse;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.mapper.StudentMapper;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.service.user.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private DtoStudentRequest requestDto;
    private DtoStudentResponse responseDto;
    private DtoStudentPublicResponse publicDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFullName("John Doe");
        student.setEmail("john@example.com");
        student.setPhone("11111");
        student.setTotalSessionsCompleted(5);
        student.setTotalSessionsScore(80);
        student.setFavoriteTopic("Science");

        requestDto = new DtoStudentRequest();
        requestDto.setFullName("Jane Doe");
        requestDto.setPhone("12345");
        requestDto.setFavoriteTopic("Math");

        responseDto = new DtoStudentResponse();
        responseDto.setId(1L);
        responseDto.setFullName("John Doe");
        responseDto.setEmail("john@example.com");
        responseDto.setPhone("11111");
        responseDto.setTotalSessionsCompleted(5);
        responseDto.setTotalSessionsScore(80);
        responseDto.setFavoriteTopic("Science");

        publicDto = new DtoStudentPublicResponse();
        publicDto.setFullName("John Doe");
        publicDto.setTotalSessionsCompleted(5);
        publicDto.setTotalSessionsScore(80);
        publicDto.setFavoriteTopic("Science");
    }

    @Test
    void testGetStudentById_whenExists_returnsStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentRepository).findById(1L);
    }

    @Test
    void testGetStudentById_whenNotFound_throwsRuntimeException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.getStudentById(2L));
        assertTrue(ex.getMessage().contains("Student not found with id: 2"));
        verify(studentRepository).findById(2L);
    }

    @Test
    void testGetDtoById_whenExists_returnsDto() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toResponse(student)).thenReturn(responseDto);

        DtoStudentResponse dto = studentService.getDtoById(1L);

        assertNotNull(dto);
        assertEquals("John Doe", dto.getFullName());
        verify(studentRepository).findById(1L);
        verify(studentMapper).toResponse(student);
    }

    @Test
    void testGetDtoById_whenNotFound_throwsRuntimeException() {
        when(studentRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.getDtoById(3L));
        verify(studentRepository).findById(3L);
    }

    @Test
    void testUpdateStudentFromDto_updatesAndReturnsDto() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student updatedEntity = new Student();
        updatedEntity.setFullName(requestDto.getFullName());
        updatedEntity.setPhone(requestDto.getPhone());
        updatedEntity.setFavoriteTopic(requestDto.getFavoriteTopic());
        when(studentMapper.toUpdateEntity(requestDto)).thenReturn(updatedEntity);

        when(studentRepository.save(student)).thenReturn(student);

        DtoStudentResponse updatedDto = new DtoStudentResponse();
        updatedDto.setId(1L);
        updatedDto.setFullName("Jane Doe");
        updatedDto.setEmail("john@example.com");
        updatedDto.setPhone("12345");
        updatedDto.setTotalSessionsCompleted(5);
        updatedDto.setTotalSessionsScore(80);
        updatedDto.setFavoriteTopic("Math");
        when(studentMapper.toResponse(student)).thenReturn(updatedDto);

        DtoStudentResponse result = studentService.updateStudentFromDto(1L, requestDto);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getFullName());
        assertEquals("12345", result.getPhone());
        verify(studentRepository).findById(1L);
        verify(studentMapper).toUpdateEntity(requestDto);
        verify(studentRepository).save(student);
        verify(studentMapper).toResponse(student);
    }

    @Test
    void testDeleteStudent_deletesSuccessfully() {
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void testDeleteStudent_whenNotFound_throwsException() {
        doThrow(new EmptyResultDataAccessException(1)).when(studentRepository).deleteById(2L);

        assertThrows(EmptyResultDataAccessException.class,
                () -> studentService.deleteStudent(2L));
        verify(studentRepository).deleteById(2L);
    }

    @Test
    void testGetPublicStudentDto_whenExists_returnsPublicDto() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toPublicResponse(student)).thenReturn(publicDto);

        DtoStudentPublicResponse dto = studentService.getPublicStudentDto(1L);

        assertNotNull(dto);
        assertEquals("John Doe", dto.getFullName());
        verify(studentRepository).findById(1L);
        verify(studentMapper).toPublicResponse(student);
    }

    @Test
    void testGetPublicStudentDto_whenNotFound_throwsRuntimeException() {
        when(studentRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> studentService.getPublicStudentDto(4L));
        verify(studentRepository).findById(4L);
    }
}

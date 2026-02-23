package personalized_learning_hub;

import com.personalized_learning_hub.dto.auth.*;
import com.personalized_learning_hub.entity.Student;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.entity.User;
import com.personalized_learning_hub.enums.Role;
import com.personalized_learning_hub.mapper.StudentMapper;
import com.personalized_learning_hub.mapper.TutorMapper;
import com.personalized_learning_hub.mapper.UserMapper;
import com.personalized_learning_hub.repository.StudentRepository;
import com.personalized_learning_hub.repository.TutorRepository;
import com.personalized_learning_hub.repository.UserRepository;
import com.personalized_learning_hub.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TutorMapper tutorMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;
    private Student mockStudent;
    private Tutor mockTutor;
    private LoginRequest loginRequest;
    private RegisterStudentRequest registerStudentRequest;
    private RegisterTutorRequest registerTutorRequest;
    private UpdateCredentialsRequest updateCredentialsRequest;
    private DtoUserResponse dtoUserResponse;

    @BeforeEach
    void setUp() {
        // Mock User
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(Role.STUDENT);

        // Mock Student
        mockStudent = new Student();
        mockStudent.setId(1L);
        mockStudent.setUser(mockUser);

        // Mock Tutor
        mockTutor = new Tutor();
        mockTutor.setId(1L);
        mockTutor.setUser(mockUser);

        // Login Request
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("plainPassword");

        // Register Student Request
        registerStudentRequest = new RegisterStudentRequest();
        registerStudentRequest.setFullName("John Doe");
        registerStudentRequest.setEmail("student@example.com");
        registerStudentRequest.setPassword("studentPassword");

        // Register Tutor Request
        registerTutorRequest = new RegisterTutorRequest();
        registerTutorRequest.setFullName("Jane Smith");
        registerTutorRequest.setEmail("tutor@example.com");
        registerTutorRequest.setPassword("tutorPassword");

        // Update Credentials Request
        updateCredentialsRequest = new UpdateCredentialsRequest();
        updateCredentialsRequest.setOldPassword("oldPassword");
        updateCredentialsRequest.setNewPassword("newPassword");
        updateCredentialsRequest.setNewEmail("newemail@example.com");

        // DTO User Response
        dtoUserResponse = new DtoUserResponse();
        dtoUserResponse.setId(1L);
        dtoUserResponse.setFullName("Test User");
        dtoUserResponse.setEmail("test@example.com");
        dtoUserResponse.setRole(Role.STUDENT);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnDtoUserResponse() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(userMapper.toDto(mockUser)).thenReturn(dtoUserResponse);
        when(studentRepository.findByUser(mockUser)).thenReturn(Optional.of(mockStudent));

        // When
        DtoUserResponse result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Role.STUDENT, result.getRole());
        assertEquals(1L, result.getStudentId());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("plainPassword", "encodedPassword");
        verify(userMapper).toDto(mockUser);
        verify(studentRepository).findByUser(mockUser);
    }

    @Test
    void login_WithValidCredentialsForTutor_ShouldReturnDtoUserResponseWithTutorId() {
        // Given
        mockUser.setRole(Role.TUTOR);
        dtoUserResponse.setRole(Role.TUTOR);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(userMapper.toDto(mockUser)).thenReturn(dtoUserResponse);
        when(tutorRepository.findByUser(mockUser)).thenReturn(Optional.of(mockTutor));

        // When
        DtoUserResponse result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(Role.TUTOR, result.getRole());
        assertEquals(1L, result.getTutorId());

        verify(tutorRepository).findByUser(mockUser);
        verify(studentRepository, never()).findByUser(any());
    }

    @Test
    void login_WithInvalidEmail_ShouldThrowRuntimeException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("Kullanıcı bulunamadı", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowRuntimeException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("Şifre yanlış", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("plainPassword", "encodedPassword");
    }

    @Test
    void login_WhenStudentNotFound_ShouldThrowRuntimeException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(userMapper.toDto(mockUser)).thenReturn(dtoUserResponse);
        when(studentRepository.findByUser(mockUser)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("Öğrenci bulunamadı", exception.getMessage());
    }

    @Test
    void registerStudent_WithValidRequest_ShouldCreateUserAndStudent() {
        // Given
        User newUser = new User();
        Student newStudent = new Student();

        when(passwordEncoder.encode("studentPassword")).thenReturn("encodedStudentPassword");
        when(userMapper.toEntity(registerStudentRequest, "encodedStudentPassword")).thenReturn(newUser);
        when(studentMapper.toEntity(registerStudentRequest, newUser)).thenReturn(newStudent);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(studentRepository.save(newStudent)).thenReturn(newStudent);

        // When
        authService.registerStudent(registerStudentRequest);

        // Then
        verify(passwordEncoder).encode("studentPassword");
        verify(userMapper).toEntity(registerStudentRequest, "encodedStudentPassword");
        verify(studentMapper).toEntity(registerStudentRequest, newUser);
        verify(userRepository).save(newUser);
        verify(studentRepository).save(newStudent);
    }

    @Test
    void registerTutor_WithValidRequest_ShouldCreateUserAndTutor() {
        // Given
        User newUser = new User();
        Tutor newTutor = new Tutor();

        when(passwordEncoder.encode("tutorPassword")).thenReturn("encodedTutorPassword");
        when(userMapper.toEntity(registerTutorRequest, "encodedTutorPassword")).thenReturn(newUser);
        when(tutorMapper.toEntity(registerTutorRequest, newUser)).thenReturn(newTutor);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(tutorRepository.save(newTutor)).thenReturn(newTutor);

        // When
        authService.registerTutor(registerTutorRequest);

        // Then
        verify(passwordEncoder).encode("tutorPassword");
        verify(userMapper).toEntity(registerTutorRequest, "encodedTutorPassword");
        verify(tutorMapper).toEntity(registerTutorRequest, newUser);
        verify(userRepository).save(newUser);
        verify(tutorRepository).save(newTutor);
    }

    @Test
    void updateCredentials_WithValidOldPassword_ShouldUpdateUserCredentials() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // When
        authService.updateCredentials(1L, updateCredentialsRequest);

        // Then
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches("oldPassword", "encodedPassword");
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(mockUser);

        // Verify that user fields were updated
        assertEquals("encodedNewPassword", mockUser.getPassword());
        assertEquals("newemail@example.com", mockUser.getEmail());
    }

    @Test
    void updateCredentials_WithOnlyPasswordUpdate_ShouldUpdateOnlyPassword() {
        // Given
        updateCredentialsRequest.setNewEmail(null);
        String originalEmail = mockUser.getEmail();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // When
        authService.updateCredentials(1L, updateCredentialsRequest);

        // Then
        assertEquals("encodedNewPassword", mockUser.getPassword());
        assertEquals(originalEmail, mockUser.getEmail()); // Email should remain unchanged
    }

    @Test
    void updateCredentials_WithOnlyEmailUpdate_ShouldUpdateOnlyEmail() {
        // Given
        updateCredentialsRequest.setNewPassword(null);
        String originalPassword = mockUser.getPassword();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // When
        authService.updateCredentials(1L, updateCredentialsRequest);

        // Then
        assertEquals(originalPassword, mockUser.getPassword()); // Password should remain unchanged
        assertEquals("newemail@example.com", mockUser.getEmail());
        verify(passwordEncoder, never()).encode("newPassword");
    }

    @Test
    void updateCredentials_WithBlankNewPassword_ShouldNotUpdatePassword() {
        // Given
        updateCredentialsRequest.setNewPassword("   "); // Blank password
        String originalPassword = mockUser.getPassword();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // When
        authService.updateCredentials(1L, updateCredentialsRequest);

        // Then
        assertEquals(originalPassword, mockUser.getPassword()); // Password should remain unchanged
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void updateCredentials_WithUserNotFound_ShouldThrowRuntimeException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.updateCredentials(1L, updateCredentialsRequest));

        assertEquals("Kullanıcı bulunamadı", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void updateCredentials_WithInvalidOldPassword_ShouldThrowRuntimeException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.updateCredentials(1L, updateCredentialsRequest));

        assertEquals("Eski şifre hatalı", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches("oldPassword", "encodedPassword");
        verify(userRepository, never()).save(any());
    }
}
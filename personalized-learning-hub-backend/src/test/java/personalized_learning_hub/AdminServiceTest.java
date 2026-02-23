package personalized_learning_hub;

import com.personalized_learning_hub.entity.*;
import com.personalized_learning_hub.repository.*;
import com.personalized_learning_hub.service.admin.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AttendanceAlertRepository attendanceAlertRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Student testStudent;
    private Tutor testTutor;
    private Session testSession;
    private Payment testPayment;
    private AttendanceAlert testAttendanceAlert;

    @BeforeEach
    void setUp() {
        // Test verilerini hazırla
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFullName("Test Student");

        testTutor = new Tutor();
        testTutor.setId(1L);
        testTutor.setName("Test Tutor");

        testSession = new Session();
        testSession.setId(1L);

        testPayment = new Payment();
        testPayment.setId(1L);

        testAttendanceAlert = new AttendanceAlert();
        testAttendanceAlert.setId(1L);
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        // Given
        List<Student> expectedStudents = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // When
        List<Student> actualStudents = adminService.getAllStudents();

        // Then
        assertNotNull(actualStudents);
        assertEquals(1, actualStudents.size());
        assertEquals(testStudent.getId(), actualStudents.get(0).getId());
        assertEquals(testStudent.getFullName(), actualStudents.get(0).getFullName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAllStudents_ShouldReturnEmptyList_WhenNoStudentsExist() {
        // Given
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Student> actualStudents = adminService.getAllStudents();

        // Then
        assertNotNull(actualStudents);
        assertTrue(actualStudents.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAllTutors_ShouldReturnAllTutors() {
        // Given
        List<Tutor> expectedTutors = Arrays.asList(testTutor);
        when(tutorRepository.findAll()).thenReturn(expectedTutors);

        // When
        List<Tutor> actualTutors = adminService.getAllTutors();

        // Then
        assertNotNull(actualTutors);
        assertEquals(1, actualTutors.size());
        assertEquals(testTutor.getId(), actualTutors.get(0).getId());
        assertEquals(testTutor.getName(), actualTutors.get(0).getName());
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    void getAllTutors_ShouldReturnEmptyList_WhenNoTutorsExist() {
        // Given
        when(tutorRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Tutor> actualTutors = adminService.getAllTutors();

        // Then
        assertNotNull(actualTutors);
        assertTrue(actualTutors.isEmpty());
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    void getAllSessions_ShouldReturnAllSessions() {
        // Given
        List<Session> expectedSessions = Arrays.asList(testSession);
        when(sessionRepository.findAll()).thenReturn(expectedSessions);

        // When
        List<Session> actualSessions = adminService.getAllSessions();

        // Then
        assertNotNull(actualSessions);
        assertEquals(1, actualSessions.size());
        assertEquals(testSession.getId(), actualSessions.get(0).getId());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void getAllSessions_ShouldReturnEmptyList_WhenNoSessionsExist() {
        // Given
        when(sessionRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Session> actualSessions = adminService.getAllSessions();

        // Then
        assertNotNull(actualSessions);
        assertTrue(actualSessions.isEmpty());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void getAllPayments_ShouldReturnAllPayments() {
        // Given
        List<Payment> expectedPayments = Arrays.asList(testPayment);
        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        // When
        List<Payment> actualPayments = adminService.getAllPayments();

        // Then
        assertNotNull(actualPayments);
        assertEquals(1, actualPayments.size());
        assertEquals(testPayment.getId(), actualPayments.get(0).getId());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void getAllPayments_ShouldReturnEmptyList_WhenNoPaymentsExist() {
        // Given
        when(paymentRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Payment> actualPayments = adminService.getAllPayments();

        // Then
        assertNotNull(actualPayments);
        assertTrue(actualPayments.isEmpty());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void getAllAttendanceAlerts_ShouldReturnAllAttendanceAlerts() {
        // Given
        List<AttendanceAlert> expectedAlerts = Arrays.asList(testAttendanceAlert);
        when(attendanceAlertRepository.findAll()).thenReturn(expectedAlerts);

        // When
        List<AttendanceAlert> actualAlerts = adminService.getAllAttendanceAlerts();

        // Then
        assertNotNull(actualAlerts);
        assertEquals(1, actualAlerts.size());
        assertEquals(testAttendanceAlert.getId(), actualAlerts.get(0).getId());
        verify(attendanceAlertRepository, times(1)).findAll();
    }

    @Test
    void getAllAttendanceAlerts_ShouldReturnEmptyList_WhenNoAlertsExist() {
        // Given
        when(attendanceAlertRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<AttendanceAlert> actualAlerts = adminService.getAllAttendanceAlerts();

        // Then
        assertNotNull(actualAlerts);
        assertTrue(actualAlerts.isEmpty());
        verify(attendanceAlertRepository, times(1)).findAll();
    }
}
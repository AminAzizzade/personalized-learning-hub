package personalized_learning_hub;

import com.personalized_learning_hub.dto.session.DtoProgressResponse;
import com.personalized_learning_hub.dto.session.DtoProgressWrapper;
import com.personalized_learning_hub.dto.session.DtoSessionRequest;
import com.personalized_learning_hub.dto.session.DtoSessionResponse;
import com.personalized_learning_hub.entity.Resource;
import com.personalized_learning_hub.entity.Session;
import com.personalized_learning_hub.entity.SessionReservation;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.enums.SessionStatus;
import com.personalized_learning_hub.mapper.SessionMapper;
import com.personalized_learning_hub.repository.ResourceRepository;
import com.personalized_learning_hub.repository.SessionRepository;
import com.personalized_learning_hub.repository.SessionReservationRepository;
import com.personalized_learning_hub.repository.TutorRepository;
import com.personalized_learning_hub.service.user.impl.SessionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private SessionReservationRepository reservationRepository;
    @Mock private TutorRepository tutorRepository;
    @Mock private ResourceRepository resourceRepository;
    @Mock private SessionMapper sessionMapper;

    @InjectMocks private SessionServiceImpl sessionService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
    }

    @Test
    void testUpdateSessionStatus_Success() {
        Long id = 1L;
        DtoSessionRequest request = new DtoSessionRequest();
        request.setStatus(SessionStatus.CONFIRMED);
        Session session = new Session(); session.setId(id); session.setStatus(SessionStatus.PENDING_APPROVAL);
        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        sessionService.updateSessionStatus(id, request);

        assertEquals(SessionStatus.CONFIRMED, session.getStatus());
        verify(sessionRepository).save(session);
    }

    @Test
    void testUpdateSessionStatus_NotFound() {
        when(sessionRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> sessionService.updateSessionStatus(2L, new DtoSessionRequest()));
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testDeleteSession() {
        sessionService.deleteSession(3L);
        verify(sessionRepository).deleteById(3L);
    }

    @Test
    void testGetSessionsByStudentId_Success() {
        Long studentId = 4L;
        Session session = new Session(); session.setId(10L);
        DtoSessionResponse dto = new DtoSessionResponse(); dto.setId(10L);
        when(sessionRepository.findByStudentId(studentId)).thenReturn(List.of(session));
        when(sessionMapper.toResponse(session)).thenReturn(dto);

        List<DtoSessionResponse> result = sessionService.getSessionsByStudentId(studentId);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
    }

    @Test
    void testGetSessionsByStudentId_Empty() {
        when(sessionRepository.findByStudentId(5L)).thenReturn(List.of());
        assertTrue(sessionService.getSessionsByStudentId(5L).isEmpty());
    }





    // ========== Pending Sessions for Tutor ==========
    @Test
    void testGetPendingSessionsDto_Success() {
        Long tutorId = 10L;
        Session session = new Session(); session.setId(400L); Date sd = new Date(); session.setStartDate(sd);
        DtoSessionResponse dto = new DtoSessionResponse(); dto.setId(400L); dto.setStartDate(sd);
        when(sessionRepository.findByTutorIdAndStatus(tutorId, SessionStatus.PENDING_APPROVAL))
                .thenReturn(List.of(session));
        when(sessionMapper.toResponse(session)).thenReturn(dto);

        List<DtoSessionResponse> result = sessionService.getPendingSessionsDto(tutorId);
        assertEquals(1, result.size());
        assertEquals(400L, result.get(0).getId());
        assertSame(sd, result.get(0).getStartDate());
    }

    @Test
    void testApproveSession_TutorIncrement() {
        Long sid = 11L;
        Session session = new Session(); session.setId(sid); session.setStatus(SessionStatus.PENDING_APPROVAL);
        Tutor tutor = new Tutor(); tutor.setTotalStudent(5);
        session.setTutor(tutor);
        when(sessionRepository.findById(sid)).thenReturn(Optional.of(session));

        sessionService.approveSession(sid);

        assertEquals(SessionStatus.CONFIRMED, session.getStatus());
        assertEquals(6, tutor.getTotalStudent());
        verify(tutorRepository).save(tutor);
    }

    @Test
    void testApproveSession_NoTutor() {
        Long sid = 12L;
        Session session = new Session(); session.setId(sid); session.setStatus(SessionStatus.PENDING_APPROVAL);
        session.setTutor(null);
        when(sessionRepository.findById(sid)).thenReturn(Optional.of(session));

        sessionService.approveSession(sid);

        assertEquals(SessionStatus.CONFIRMED, session.getStatus());
        verify(tutorRepository, never()).save(any());
    }



    // ========== Progress by Tutor ==========
    @Test
    void testGetSessionProgressByTutorId_ResourcesAndReservations() {
        Long tutorId = 14L;
        Session session = new Session(); session.setId(700L); Date sd = new Date(); session.setStartDate(sd);
        Resource res = mock(Resource.class);
        when(res.getSession()).thenReturn(session);
        SessionReservation reserv = mock(SessionReservation.class);
        when(reserv.getSession()).thenReturn(session);
        when(resourceRepository.findResourcesByTutorId(tutorId)).thenReturn(List.of(res));
        when(reservationRepository.findBySession_Tutor_Id(tutorId)).thenReturn(List.of(reserv));
        DtoProgressResponse p1 = new DtoProgressResponse();
        DtoProgressResponse p2 = new DtoProgressResponse();
        when(sessionMapper.ResourceToProgress(res)).thenReturn(p1);
        when(sessionMapper.ReservationToProgress(reserv)).thenReturn(p2);

        List<DtoProgressWrapper> wrappers = sessionService.getSessionProgressByTutorId(tutorId);
        assertEquals(1, wrappers.size());
        DtoProgressWrapper w = wrappers.get(0);
        assertEquals(700L, w.getSessionId());
        assertSame(sd, w.getStartDate());
        assertTrue(w.getProgressList().contains(p1));
        assertTrue(w.getProgressList().contains(p2));
    }

    @Test
    void testGetSessionProgressByTutorId_ResourceOnly() {
        Long tutorId = 15L;
        Session session = new Session(); session.setId(800L); Date sd = new Date(); session.setStartDate(sd);
        Resource res = mock(Resource.class);
        when(res.getSession()).thenReturn(session);
        when(resourceRepository.findResourcesByTutorId(tutorId)).thenReturn(List.of(res));
        when(reservationRepository.findBySession_Tutor_Id(tutorId)).thenReturn(List.of());
        DtoProgressResponse p = new DtoProgressResponse();
        when(sessionMapper.ResourceToProgress(res)).thenReturn(p);

        List<DtoProgressWrapper> wrappers = sessionService.getSessionProgressByTutorId(tutorId);
        assertEquals(1, wrappers.size());
        DtoProgressWrapper w = wrappers.get(0);
        assertEquals(800L, w.getSessionId());
        assertSame(sd, w.getStartDate());
        assertEquals(1, w.getProgressList().size());
    }

    @Test
    void testGetSessionProgressByTutorId_ReservationOnly() {
        Long tutorId = 16L;
        Session session = new Session(); session.setId(900L); Date sd = new Date(); session.setStartDate(sd);
        when(resourceRepository.findResourcesByTutorId(tutorId)).thenReturn(List.of());
        SessionReservation reserv = mock(SessionReservation.class);
        when(reserv.getSession()).thenReturn(session);
        when(reservationRepository.findBySession_Tutor_Id(tutorId)).thenReturn(List.of(reserv));
        DtoProgressResponse p = new DtoProgressResponse();
        when(sessionMapper.ReservationToProgress(reserv)).thenReturn(p);

        List<DtoProgressWrapper> wrappers = sessionService.getSessionProgressByTutorId(tutorId);
        assertEquals(1, wrappers.size());
        DtoProgressWrapper w = wrappers.get(0);
        assertEquals(900L, w.getSessionId());
        assertSame(sd, w.getStartDate());
        assertEquals(1, w.getProgressList().size());
    }

    @Test
    void testGetSessionProgressByTutorId_NoData() {
        when(resourceRepository.findResourcesByTutorId(17L)).thenReturn(List.of());
        when(reservationRepository.findBySession_Tutor_Id(17L)).thenReturn(List.of());
        assertTrue(sessionService.getSessionProgressByTutorId(17L).isEmpty());
    }    @Test
    void testCancelSession_Success() {
        Long sid = 18L;
        Session session = new Session();
        session.setId(sid);
        session.setStatus(SessionStatus.CONFIRMED);
        when(sessionRepository.findById(sid)).thenReturn(Optional.of(session));

        sessionService.cancelSession(sid);

        assertEquals(SessionStatus.CANCELLED, session.getStatus());
        verify(sessionRepository).save(session);
    }

    @Test
    void testCancelSession_NotFound() {
        Long sid = 19L;
        when(sessionRepository.findById(sid)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sessionService.cancelSession(sid));
        verify(sessionRepository, never()).save(any());
    }
}

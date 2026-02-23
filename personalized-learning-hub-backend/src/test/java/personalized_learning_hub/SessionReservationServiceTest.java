package personalized_learning_hub;

import com.personalized_learning_hub.dto.session.DtoSessionReservationRequest;
import com.personalized_learning_hub.dto.session.DtoSessionReservationResponse;
import com.personalized_learning_hub.entity.SessionReservation;
import com.personalized_learning_hub.enums.SessionReservationStatus;
import com.personalized_learning_hub.mapper.SessionReservationMapper;
import com.personalized_learning_hub.repository.SessionReservationRepository;
import com.personalized_learning_hub.service.user.impl.SessionReservationServiceImpl;
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
class SessionReservationServiceTest {

    @Mock
    private SessionReservationRepository reservationRepository;

    @Mock
    private SessionReservationMapper reservationMapper;

    @InjectMocks
    private SessionReservationServiceImpl reservationService;

    @Test
    void testCreateReservation() {
        DtoSessionReservationRequest request = new DtoSessionReservationRequest();
        // set fields on request if needed

        SessionReservation entity = new SessionReservation();
        entity.setId(1L);
        entity.setStatus(SessionReservationStatus.PENDING);

        DtoSessionReservationResponse responseDto = new DtoSessionReservationResponse();
        responseDto.setId(1L);

        when(reservationMapper.toEntity(request)).thenReturn(entity);
        when(reservationRepository.save(entity)).thenReturn(entity);
        when(reservationMapper.toDto(entity)).thenReturn(responseDto);

        DtoSessionReservationResponse result = reservationService.createReservation(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reservationMapper).toEntity(request);
        verify(reservationRepository).save(entity);
        verify(reservationMapper).toDto(entity);
    }

    @Test
    void testDeleteReservation() {
        Long id = 2L;

        // Act
        reservationService.deleteReservation(id);

        // Assert
        verify(reservationRepository).deleteById(id);
    }


    @Test
    void testUpdateReservation_NotFound() {
        Long id = 4L;
        DtoSessionReservationRequest request = new DtoSessionReservationRequest();

        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservationService.updateReservation(id, request));
        verify(reservationRepository).findById(id);
        verify(reservationMapper, never()).updateEntity(any(), any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void testGetReservationsByStudent() {
        Long studentId = 5L;
        SessionReservation entity = new SessionReservation();

        DtoSessionReservationResponse dto = new DtoSessionReservationResponse();

        when(reservationRepository.findBySession_Student_Id(studentId))
                .thenReturn(Collections.singletonList(entity));
        when(reservationMapper.toDto(entity)).thenReturn(dto);

        List<DtoSessionReservationResponse> result = reservationService.getReservationsByStudent(studentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findBySession_Student_Id(studentId);
        verify(reservationMapper).toDto(entity);
    }

    @Test
    void testGetReservationsByTutor() {
        Long tutorId = 6L;
        SessionReservation entity = new SessionReservation();

        DtoSessionReservationResponse dto = new DtoSessionReservationResponse();

        when(reservationRepository.findBySession_Tutor_Id(tutorId))
                .thenReturn(Collections.singletonList(entity));
        when(reservationMapper.toDto(entity)).thenReturn(dto);

        List<DtoSessionReservationResponse> result = reservationService.getReservationsByTutor(tutorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findBySession_Tutor_Id(tutorId);
        verify(reservationMapper).toDto(entity);
    }
}

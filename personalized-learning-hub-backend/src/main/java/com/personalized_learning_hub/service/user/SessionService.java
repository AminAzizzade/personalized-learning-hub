package com.personalized_learning_hub.service.user;

import com.personalized_learning_hub.dto.session.*;

import java.util.List;

public interface SessionService {

    // === CRUD İŞLEMLERİ ===
    void updateSessionStatus(Long sessionId, DtoSessionRequest request);
    void deleteSession(Long id);



    // === STUDENT İŞLEMLERİ ===
    List<DtoSessionResponse> getSessionsByStudentId(Long studentId);
    List<DtoProgressWrapper> getSessionProgressByStudentId(Long studentId);


    // === TUTOR İŞLEMLERİ ===
    List<DtoSessionResponse> getPendingSessionsDto(Long tutorId);
    void approveSession(Long sessionId);
    void cancelSession(Long sessionId);
    List<DtoProgressWrapper> getSessionProgressByTutorId(Long tutorId);
    List<DtoSessionResponse> getSessionsByTutorId(Long tutorId);


}


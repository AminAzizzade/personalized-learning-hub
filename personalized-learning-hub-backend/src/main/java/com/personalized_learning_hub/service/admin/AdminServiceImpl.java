package com.personalized_learning_hub.service.admin;

import com.personalized_learning_hub.entity.*;
import com.personalized_learning_hub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final SessionRepository sessionRepository;
    private final ResourceRepository resourceRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceAlertRepository attendanceAlertRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

//    @Override
//    public List<Resource> getAllResources() {
//        return resourceRepository.findAll();
//    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<AttendanceAlert> getAllAttendanceAlerts() {
        return attendanceAlertRepository.findAll();
    }
}
package com.personalized_learning_hub.controller.admin;

import com.personalized_learning_hub.dto.profil.DtoTutorPublicResponse;
import com.personalized_learning_hub.dto.system.DtoAttendanceAlertResponse;
import com.personalized_learning_hub.dto.system.DtoPaymentResponse;
import com.personalized_learning_hub.entity.AttendanceAlert;
import com.personalized_learning_hub.entity.Payment;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.mapper.*;
import com.personalized_learning_hub.mapper.*;
import com.personalized_learning_hub.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final StudentMapper studentMapper;
    private final TutorMapper tutorMapper;
    private final SessionMapper sessionMapper;
    private final ResourceMapper resourceMapper;
    private final PaymentMapper paymentMapper;
    private final AttendanceAlertMapper alertMapper;

//    @GetMapping("/students")
//    public ResponseEntity<List<DtoStudentPublicResponse>> getAllStudents() {
//        List<Student> students = adminService.getAllStudents();
//        return ResponseEntity.ok(
//                students.stream().map(studentMapper::toResponse).collect(Collectors.toList())
//        );
//    }

    @GetMapping("/tutors")
    public ResponseEntity<List<DtoTutorPublicResponse>> getAllTutors() {
        List<Tutor> tutors = adminService.getAllTutors();
        return ResponseEntity.ok(
                tutors.stream().map(tutorMapper::toTutorPublicResponse).collect(Collectors.toList())
        );
    }

//    @GetMapping("/sessions")
//    public ResponseEntity<List<DtoSessionSummaryResponse>> getAllSessions() {
//        List<Session> sessions = adminService.getAllSessions();
//        return ResponseEntity.ok(
//                sessions.stream().map(sessionMapper::toSummary).collect(Collectors.toList())
//        );
//    }


    @GetMapping("/payments")
    public ResponseEntity<List<DtoPaymentResponse>> getAllPayments() {
        List<Payment> payments = adminService.getAllPayments();
        return ResponseEntity.ok(
                payments.stream().map(paymentMapper::toResponse).collect(Collectors.toList())
        );
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<DtoAttendanceAlertResponse>> getAllAlerts() {
        List<AttendanceAlert> alerts = adminService.getAllAttendanceAlerts();
        return ResponseEntity.ok(
                alerts.stream().map(alertMapper::toDto).collect(Collectors.toList())
        );
    }
}

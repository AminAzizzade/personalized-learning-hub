package com.personalized_learning_hub.service.admin;

import com.personalized_learning_hub.entity.*;

import java.util.List;

public interface AdminService {

    List<Student> getAllStudents();
    List<Tutor> getAllTutors();
    List<Session> getAllSessions();
    //List<Resource> getAllResources();
    List<Payment> getAllPayments();
    List<AttendanceAlert> getAllAttendanceAlerts();
}
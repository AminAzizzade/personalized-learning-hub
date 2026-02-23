package com.personalized_learning_hub.entity;

import com.personalized_learning_hub.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.PENDING_APPROVAL;

    private String topic = "";
    private String totalDuration = "";
    private int hourlyRate = 0;
    private String language = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(nullable = false)
    private int totalMeetings = 0;

    @Column(nullable = false)
    private int totalPrice = 0;

    @Column(nullable = false)
    private int finalScore = 0;

    @Column(nullable = false)
    private int totalAbsence = 0;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceAlert> attendanceAlerts = List.of();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Resource> resource = List.of();


}


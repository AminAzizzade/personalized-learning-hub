package com.personalized_learning_hub.entity;

import com.personalized_learning_hub.enums.SessionReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SessionReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SessionReservationStatus status = SessionReservationStatus.PENDING;

    private LocalDateTime dateTime;

    @Column(nullable = false)
    private int score = 0;

    @Column(nullable = false)
    private int tutorEvaluationRating = 0;

    @OneToOne(mappedBy = "sessionReservation", cascade = CascadeType.ALL)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
}

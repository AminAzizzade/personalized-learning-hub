package com.personalized_learning_hub.enums;

public enum SessionReservationStatus {
    PENDING,           // Öğrenci saat seçti, tutor henüz değerlendirmedi
    COMPLETED,         // Oturum yapıldı ve işaretlendi
    STUDENT_ABSENT,    // Öğrenci gelmedi, tutor işaretledi
    CANCELED           // Tutor dersi iptal etti
}

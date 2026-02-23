package com.personalized_learning_hub.enums;


public enum SessionStatus {
    PENDING_APPROVAL, // Tutor henüz onaylamadı
    CONFIRMED,        // Tutor onayladı
    COMPLETED,        // Ders yapıldı
    CANCELLED         // İptal edildi
}
package com.personalized_learning_hub.config;

import com.personalized_learning_hub.entity.*;
import com.personalized_learning_hub.enums.*;
import com.personalized_learning_hub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContextInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final SkillAssessmentRepository skillAssessmentRepository;
    private final SessionRepository sessionRepository;
    private final SessionReservationRepository sessionReservationRepository;
    private final ResourceRepository resourceRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Student student = null;
        Tutor tutor = null;
        // 👤 Öğrenci ekle
        String studentEmail = "muhammed@gmail.com";
        if (!userRepository.existsByEmail(studentEmail)) {
            User studentUser = new User();
            studentUser.setEmail(studentEmail);
            studentUser.setFullName("Muhammed Orhantekin");
            studentUser.setRole(Role.STUDENT);
            studentUser.setPassword(passwordEncoder.encode("12345"));

            // Student Entity bağla
            student = new Student();
            student.setFullName("Muhammed Orhantekin");
            student.setEmail(studentEmail);
            student.setPhone("555-0001");
            student.setTotalSessionsCompleted(0);
            student.setTotalSessionsScore(285);
            student.setFavoriteTopic("İngilizce");
            student.setUser(studentUser);

            studentUser.setStudent(student);

            userRepository.save(studentUser); // student da cascade ile kaydedilir
            studentRepository.save(student);
            System.out.println("✅ Demo öğrenci başarıyla eklendi.");
        } else {
            System.out.println("ℹ️ Demo öğrenci zaten mevcut.");
        }

        // 👨‍🏫 Eğitmen ekle
        String tutorEmail = "yasemin@gmail.com";
        if (!userRepository.existsByEmail(tutorEmail)) {
            User tutorUser = new User();
            tutorUser.setEmail(tutorEmail);
            tutorUser.setFullName("Yasemin Topaloglu");
            tutorUser.setRole(Role.TUTOR);
            tutorUser.setPassword(passwordEncoder.encode("12345"));

            // Tutor Entity bağla
            tutor = new Tutor();
            tutor.setName("Yasemin Topaloglu");
            tutor.setEmail(tutorEmail);
            tutor.setPricePerHour(550);
            tutor.setRating(4);
            tutor.setTotalMeeting(4);
            tutor.setTotalStudent(1);
            tutor.setExpertiseTopics(List.of("Web Geliştirme", "Spring-React"));
            tutor.setPreferredStyles(List.of("Görsel"));
            tutor.setLanguage(List.of("İngilizce"));
            tutor.setAvailability(List.of("Çarşamba,09:00-10:00", "Çarşamba,10:00-11:00"));
            tutor.setUser(tutorUser);

            tutorUser.setTutor(tutor);

            userRepository.save(tutorUser); // tutor da cascade ile kaydedilir
            tutorRepository.save(tutor);
            System.out.println("✅ Demo eğitmen başarıyla eklendi.");
        } else {
            System.out.println("ℹ️ Demo eğitmen zaten mevcut.");
        }

        // 📅 Sabit başlangıç tarihi: 2025-05-12 11:00:00
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fixedStartDate = sdf.parse("2025-05-12 11:00:00");

        // 📊 Skill Assessment
        SkillAssessment assessment = new SkillAssessment(null, "Web Geliştirme", "Görsel", "İngilizce", 4,
                "500-1000", "FSE projesinden 100 almak", "3 Ay", fixedStartDate,
                List.of("Çarşamba,09:00-10:00", "Çarşamba,10:00-11:00"), student);
        skillAssessmentRepository.save(assessment);

        // 3 ay sonrası
        Calendar cal = Calendar.getInstance();
        cal.setTime(fixedStartDate);
        cal.add(Calendar.MONTH, 3);
        Date endDate = cal.getTime();

        // 📅 Session
        Session session = new Session(null, SessionStatus.CONFIRMED, "Web Geliştirme", "3 Ay",
                500, "İngilizce", fixedStartDate, endDate, 4, 2000, 285, 0,
                student, tutor, new ArrayList<>(), new ArrayList<>());
        sessionRepository.save(session);

        // 🤝 Reservations
        // 📅 Başlangıç günü: 14 Mayıs 2025 Çarşamba 09:00
        Calendar baseDate = Calendar.getInstance();
        baseDate.set(2025, Calendar.MAY, 14, 9, 0, 0); // 2025-05-14 09:00

        List<SessionReservation> reservations = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            // Date → LocalDateTime dönüşümü
            LocalDateTime dateTime = baseDate.getTime()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            SessionReservation res = new SessionReservation(
                    null, SessionReservationStatus.COMPLETED, dateTime, 50,
                    4, null, session);

            reservations.add(res);
            baseDate.add(Calendar.DAY_OF_MONTH, 7); // her hafta
        }

        // 📅 5. hafta için PENDING reservation
        LocalDateTime pendingDateTime = baseDate.getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        SessionReservation pendingRes = new SessionReservation(null, SessionReservationStatus.PENDING, pendingDateTime,
                0, 0, null, session);

        reservations.add(pendingRes);

        sessionReservationRepository.saveAll(reservations);

        // 📂 Resources
        Resource assignment = new Resource(
                null, "FSE Proje",
                "Website geliştirme",
                "PDF", false,
                "project_outline.pdf", "project_assignment.pdf",
                ResourceType.ASSIGNMENT, 85,
                new Date(System.currentTimeMillis() + 5 * 24 * 3600 * 1000),
                ResourceStatus.COMPLETED, LocalDateTime.now(), session);

        Resource material = new Resource(
                null, "Software Engineering",
                "FSE ders materyali",
                "PDF", true,
                "software_engineering.pdf", "",
                ResourceType.MATERIAL, 0,
                null, null, LocalDateTime.now(), session);

        resourceRepository.saveAll(List.of(assignment, material));
    }
}

package com.personalized_learning_hub.service.system;

import com.personalized_learning_hub.entity.SkillAssessment;
import com.personalized_learning_hub.entity.Tutor;
import com.personalized_learning_hub.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TutorAssignmentService {

    private final TutorRepository tutorRepository;

    public Tutor assignBestTutorForSkillAssessment(SkillAssessment sa) {
        List<Tutor> tutors = tutorRepository.findAll();
        Tutor bestTutor = null;
        int bestScore = -1;

        for (Tutor tutor : tutors) {

            // Boş veri kontrolü
            if (tutor.getExpertiseTopics() == null || tutor.getPreferredStyles() == null || tutor.getAvailability() == null) {
                continue;
            }

            if (tutor.getLanguage() == null || tutor.getLanguage().isEmpty()) continue;
            if (tutor.getPricePerHour() <= 0) continue;

            // ZORUNLU KRİTERLER
            if (!tutor.getExpertiseTopics().contains(sa.getTopic())) continue;
            if (!tutor.getPreferredStyles().contains(sa.getPreferredStyle())) continue;
            if (!priceInRange(sa.getPriceRange(), tutor.getPricePerHour())) continue;

            // PUANLAMA KRİTERLERİ
            int score = 0;

            for (String slot : sa.getAvailability()) {
                if (tutor.getAvailability().contains(slot)) {
                    score += 5;
                }
            }

            if (tutor.getRating() > sa.getPreferredTutorRating()) {
                score += 3;
            } else if (tutor.getRating() == sa.getPreferredTutorRating()) {
                score += 2;
            }

            if (score > bestScore) {
                bestScore = score;
                bestTutor = tutor;
            }
        }

        return bestTutor;
    }

    private boolean priceInRange(String priceRange, int price) {
        switch (priceRange) {
            case "0-250": return price <= 250;
            case "250-500": return price >= 250 && price <= 500;
            case "500-750": return price >= 500 && price <= 750;
            case "750-1000": return price >= 750 && price <= 1000;
            default: return false;
        }
    }

}
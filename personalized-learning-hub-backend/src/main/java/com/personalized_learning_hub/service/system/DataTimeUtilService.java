package com.personalized_learning_hub.service.system;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class DataTimeUtilService {

    public LocalDateTime convertToLocalDateTimeService(String input) {
        // Örnek input: "2025-06-17,cuma,09:00-10:00"
        try {
            String[] parts = input.split(",");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Beklenen format: yyyy-MM-dd,<gün>,<saat aralığı>");
            }

            String weekStartStr = parts[0].trim(); // "2025-06-17"
            String dayText = parts[1].trim().toLowerCase(); // "cuma"
            String timeRange = parts[2].trim().split("-")[0]; // "09:00" (başlangıç saati)

            LocalDate weekStartDate = LocalDate.parse(weekStartStr);

            DayOfWeek dayOfWeek = switch (dayText) {
                case "pazartesi" -> DayOfWeek.MONDAY;
                case "salı"      -> DayOfWeek.TUESDAY;
                case "çarşamba"  -> DayOfWeek.WEDNESDAY;
                case "perşembe"  -> DayOfWeek.THURSDAY;
                case "cuma"      -> DayOfWeek.FRIDAY;
                case "cumartesi" -> DayOfWeek.SATURDAY;
                case "pazar"     -> DayOfWeek.SUNDAY;
                default -> throw new IllegalArgumentException("Geçersiz gün: " + dayText);
            };

            int daysToAdd = dayOfWeek.getValue() - weekStartDate.getDayOfWeek().getValue();
            LocalDate targetDate = weekStartDate.plusDays(daysToAdd);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime time = LocalTime.parse(timeRange, formatter);

            return LocalDateTime.of(targetDate, time);

        } catch (Exception e) {
            throw new RuntimeException("Tarih formatı geçersiz: " + input, e);
        }
    }



    public Date calculateEndDate(Date startDate, String durationText) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // örn: "3 Ay"
        int months = Integer.parseInt(durationText.split(" ")[0]);
        calendar.add(Calendar.MONTH, months);

        return calendar.getTime();
    }



    public Date calculateDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }
}

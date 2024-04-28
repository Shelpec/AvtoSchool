package net.guides.springboot.registrationlogindemo.service;

import net.guides.springboot.registrationlogindemo.entity.DrivingSession;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.repository.DrivingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrivingService {
    @Autowired
    private DrivingSessionRepository sessionRepository;
    @Autowired
    private UserService userService;

    public void bookSession(DrivingSession session){
        sessionRepository.save(session);
    }


    public List<DrivingSession> getSessionsForDriver(Long driverId) {
        return sessionRepository.findByDriverId(driverId);
    }

    public List<DrivingSession> getSessionsForStudent(Long studentId) {
        return sessionRepository.findByStudentId(studentId);
    }

    public boolean hasSession(Long studentId) {
        return sessionRepository.existsByStudentId(studentId);
    }

    public boolean alreadyHasSession(String email) {
        User student = userService.findUserByEmail(email);
        if (student != null) {
            return hasSession(student.getId());
        }
        return false;
    }

    public boolean isValidSessionTime(LocalDateTime sessionTime) {
        DayOfWeek day = sessionTime.getDayOfWeek();
        LocalTime time = sessionTime.toLocalTime();
        return !time.isBefore(LocalTime.of(9, 0)) &&
                !time.isAfter(LocalTime.of(17, 0));
    }

    public boolean sessionExists(Long studentId, Long driverId) {
        return sessionRepository.existsByStudentIdAndDriverId(studentId, driverId);
    }

    public List<String> generateTimeSlots(LocalDate date, Long driverId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        int interval = 1;

        for (int i = 0; i < 9; i++) {
            slots.add(start.plusHours(i * interval));
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<LocalTime> bookedSlots = sessionRepository.findByDriverIdAndSessionTimeBetween(driverId, startOfDay, endOfDay)
                .stream()
                .map(session -> session.getSessionTime().toLocalTime())
                .collect(Collectors.toList());

        slots.removeIf(bookedSlots::contains);

        return slots.stream().map(time -> time.format(formatter)).collect(Collectors.toList());
    }
}


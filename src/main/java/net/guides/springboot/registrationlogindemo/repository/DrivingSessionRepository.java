package net.guides.springboot.registrationlogindemo.repository;

import net.guides.springboot.registrationlogindemo.entity.DrivingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {
    List<DrivingSession> findByDriverId(Long driverId);
    List<DrivingSession> findByStudentId(Long studentId);
    boolean existsByStudentId(Long studentId);

    boolean existsByStudentIdAndDriverId(Long studentId, Long driverId);

    List<DrivingSession> findByDriverIdAndSessionTimeBetween(Long driverId, LocalDateTime start, LocalDateTime end);

}


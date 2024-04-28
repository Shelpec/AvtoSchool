package net.guides.springboot.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "driving_session", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "driver_id"})
})
public class DrivingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User student;

    private String location;

    private LocalDateTime sessionTime;

    // Constructors, Getters and Setters...
}


package net.guides.springboot.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Изменено для связи с таблицей пользователей

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name = "is_completed")
    private Boolean isCompleted;

}

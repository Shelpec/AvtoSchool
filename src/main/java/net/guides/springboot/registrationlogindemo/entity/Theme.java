package net.guides.springboot.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "themes")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String videoUrl;

    @OneToOne(mappedBy = "theme")
    private Test test;
}


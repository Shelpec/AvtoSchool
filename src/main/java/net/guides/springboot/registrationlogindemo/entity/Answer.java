package net.guides.springboot.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
}

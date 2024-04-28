package net.guides.springboot.registrationlogindemo.forms;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionForm {
    private String content;
    private List<String> answers = new ArrayList<>();
    private String correctAnswer;
    // Геттеры и сеттеры
}
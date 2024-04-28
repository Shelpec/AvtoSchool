package net.guides.springboot.registrationlogindemo.forms;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestForm {
    private String name;
    private List<QuestionForm> questions = new ArrayList<>();
    // Геттеры и сеттеры
}

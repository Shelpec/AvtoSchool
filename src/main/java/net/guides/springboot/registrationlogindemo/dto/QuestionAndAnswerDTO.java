package net.guides.springboot.registrationlogindemo.dto;

import net.guides.springboot.registrationlogindemo.entity.Question;

public class QuestionAndAnswerDTO {
    private Question question;
    private String userAnswer;
    private String correctAnswer;

    public QuestionAndAnswerDTO(Question question, String userAnswer) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = question.getCorrectAnswer();
    }

    // Getters
    public Question getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}


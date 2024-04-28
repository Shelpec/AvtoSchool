package net.guides.springboot.registrationlogindemo.repository;

import net.guides.springboot.registrationlogindemo.entity.Question;
import net.guides.springboot.registrationlogindemo.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findQuestionByTest(Test test);

}


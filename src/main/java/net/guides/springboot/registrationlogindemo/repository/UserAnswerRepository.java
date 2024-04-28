package net.guides.springboot.registrationlogindemo.repository;

import net.guides.springboot.registrationlogindemo.entity.Test;
import net.guides.springboot.registrationlogindemo.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByUserIdAndQuestionId(Long userId, Long questionId);
    List<UserAnswer> findByUserIdAndQuestion_Test_TestId(Long userId, Long test);
}


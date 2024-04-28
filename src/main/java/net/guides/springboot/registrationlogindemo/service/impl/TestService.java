package net.guides.springboot.registrationlogindemo.service.impl;
import net.guides.springboot.registrationlogindemo.entity.UserAnswer;
import net.guides.springboot.registrationlogindemo.entity.Answer;
import net.guides.springboot.registrationlogindemo.entity.Question;
import net.guides.springboot.registrationlogindemo.entity.Test;
import net.guides.springboot.registrationlogindemo.forms.QuestionForm;
import net.guides.springboot.registrationlogindemo.forms.TestForm;
import net.guides.springboot.registrationlogindemo.repository.AnswerRepository;
import net.guides.springboot.registrationlogindemo.repository.QuestionRepository;
import net.guides.springboot.registrationlogindemo.repository.TestRepository;
import net.guides.springboot.registrationlogindemo.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
    public class TestService {

        @Autowired
        private TestRepository testRepository;

        @Autowired
        private QuestionRepository questionRepository;

        @Autowired
        private UserAnswerRepository userAnswerRepository;

        public void createTestWithQuestionsAndAnswers(TestForm testForm) {
            Test test = new Test();
            test.setTestName(testForm.getName());

            for (QuestionForm qf : testForm.getQuestions()) {
                Question question = new Question();
                question.setContent(qf.getContent());
                question.setTest(test);
                question.setCorrectAnswer(qf.getCorrectAnswer());

                List<Answer> answers = qf.getAnswers().stream().map(answerContent -> {
                    Answer answer = new Answer();
                    answer.setContent(answerContent);
                    return answer;
                }).collect(Collectors.toList());

                question.setAnswers(answers);
                test.getQuestions().add(question);
            }

            testRepository.save(test);
        }
    public boolean isTestPassed(Long userId, Long testId) {
        Optional<Test> testOptional = testRepository.findById(testId);
        Test test = testOptional.get();

        // Получить список вопросов для данного теста
        List<Question> questions = test.getQuestions();
        for (Question question : questions) {
            List<UserAnswer> answers = userAnswerRepository.findByUserIdAndQuestionId(userId, question.getId());
            if (answers.isEmpty() || answers.get(0).getUserAnswer()==null) {
                return false;
            }
        }
        return true;
    }

    public void resetTest(Long userId, Long testId) {
        List<UserAnswer> answers = userAnswerRepository.findByUserIdAndQuestion_Test_TestId(userId, testId);
        userAnswerRepository.deleteAll(answers);  // Удаление всех ответов пользователя для данного теста
    }
}


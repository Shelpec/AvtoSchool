package net.guides.springboot.registrationlogindemo.controller;

import net.guides.springboot.registrationlogindemo.dto.QuestionAndAnswerDTO;
import net.guides.springboot.registrationlogindemo.entity.Question;
import net.guides.springboot.registrationlogindemo.entity.Test;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.entity.UserAnswer;
import net.guides.springboot.registrationlogindemo.forms.TestForm;
import net.guides.springboot.registrationlogindemo.repository.*;
import net.guides.springboot.registrationlogindemo.service.impl.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class TestController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserRepository userRepository;
    private final Map<Long, String> userAnswers = new HashMap<>();

    @GetMapping("/test")
    public String showTest(Model model) {
        List<Test> tests = testRepository.findAll();
        model.addAttribute("tests", tests);
        return "test-list";
    }

    @GetMapping("/test/{testId}")
    public String showTest(@PathVariable Long testId, Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        // Получить тест по его идентификатору
        Optional<Test> testOptional = testRepository.findById(testId);
        if(testService.isTestPassed(user.getId(), testId)){
            return "redirect:/test/results/{testId}";
        }
        // Проверить, существует ли тест с данным идентификатором
        if (testOptional.isPresent()) {
            Test test = testOptional.get();

            // Получить список вопросов для данного теста
            List<Question> questions = test.getQuestions();

            model.addAttribute("questions", questions);
            model.addAttribute("testName", test.getTestName());

            return "test";
        } else {
            return null;
        }
    }
    @GetMapping("/test/results/{testId}")
    public String viewTestResults(@PathVariable Long testId, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        List<UserAnswer> userAnswers = userAnswerRepository.findByUserIdAndQuestion_Test_TestId(user.getId(), testId);
        Optional<Test> testOptional = testRepository.findById(testId);

        if (!testOptional.isPresent()) {
            model.addAttribute("message", "Тест не найден");
            return "errorPage";
        }

        Test test = testOptional.get();
        List<QuestionAndAnswerDTO> qaList = new ArrayList<>();
        test.getQuestions().forEach(question -> {
            String userAnswer = userAnswers.stream()
                    .filter(ua -> ua.getQuestion().equals(question))
                    .findFirst()
                    .map(UserAnswer::getUserAnswer)
                    .orElse("Не ответил");
            qaList.add(new QuestionAndAnswerDTO(question, userAnswer));
        });

        model.addAttribute("qaList", qaList);
        model.addAttribute("totalQuestions", test.getQuestions().size());
        model.addAttribute("correctAnswers", userAnswers.stream().filter(ua -> ua.getUserAnswer().equals(ua.getQuestion().getCorrectAnswer())).count());
        model.addAttribute("score", calculateScore(userAnswers, test.getQuestions().size()));
        model.addAttribute("test", test);

        return "result";
    }

    private double calculateScore(List<UserAnswer> userAnswers, int totalQuestions) {
        long correctCount = userAnswers.stream().filter(ua -> ua.getUserAnswer().equals(ua.getQuestion().getCorrectAnswer())).count();
        return (double) correctCount / totalQuestions * 100.0;
    }




    @PostMapping("/test/{testId}/submit")
    public String submitTest(@PathVariable Long testId, @RequestParam Map<String, String> answers, Model model,Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Test> testOptional = testRepository.findById(testId);
        List<Question> questions = null;
        int correctCount = 0;
        if (testOptional.isPresent()) {
            Test test = testOptional.get();

            // Получить список вопросов для данного теста
            questions = test.getQuestions();
        }
        List<UserAnswer> userAnswers = new ArrayList<>();

        for (Question question : questions) {
            String userAnswerContent = answers.getOrDefault("question_" + question.getId(), "");
            if (userAnswerContent.equals(question.getCorrectAnswer())) {
                correctCount++;
            }
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUser(user);
            userAnswer.setQuestion(question);
            userAnswer.setUserAnswer(userAnswerContent);
            userAnswerRepository.save(userAnswer);

            userAnswers.add(userAnswer);
        }
        double score = ((double) correctCount / questions.size()) * 100;
        model.addAttribute("score", score);
        model.addAttribute("totalQuestions", questions.size());
        model.addAttribute("correctAnswers", correctCount);
        return "redirect:/test/results/{testId}";
    }

    @PostMapping("/test/{testId}/reset")
    public String resetTest(@PathVariable Long testId, Authentication authentication, RedirectAttributes redirectAttributes) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        testService.resetTest(user.getId(), testId);
        redirectAttributes.addFlashAttribute("successMessage", "Тест был сброшен. Вы можете пройти его снова.");
        return "redirect:/test/" + testId;
    }


    @GetMapping("/tests/create")
    public String showCreateTestForm(Model model) {
        model.addAttribute("test", new Test());
        return "create-test";
    }

    @PostMapping("/tests/create")
    public String createTest(@ModelAttribute TestForm testForm) {
        testService.createTestWithQuestionsAndAnswers(testForm);
        return "redirect:/test";
    }

}

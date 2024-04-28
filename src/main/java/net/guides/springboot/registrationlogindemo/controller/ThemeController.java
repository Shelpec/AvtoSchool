package net.guides.springboot.registrationlogindemo.controller;

import net.guides.springboot.registrationlogindemo.entity.Test;
import net.guides.springboot.registrationlogindemo.entity.Theme;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.entity.UserProgress;
import net.guides.springboot.registrationlogindemo.repository.UserProgressRepository;
import net.guides.springboot.registrationlogindemo.repository.UserRepository;
import net.guides.springboot.registrationlogindemo.service.ThemeService;
import net.guides.springboot.registrationlogindemo.service.UserProgressService;
import net.guides.springboot.registrationlogindemo.service.impl.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ThemeController {
    @Autowired
    private ThemeService themeService;
    @Autowired
    private UserProgressService userProgressService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private UserProgressRepository userProgressRepository;
    @GetMapping("/themes")
    public String listThemes(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            return "redirect:/login"; // Перенаправление на страницу входа, если пользователь не найден
        }
        List<UserProgress> completedThemes = userProgressService.findCompletedThemesByUser(user);
        int totalThemes = themeService.countAllThemes();
        String progress = completedThemes.size() + " из " + totalThemes;
        model.addAttribute("themes", themeService.findAllThemes());
        model.addAttribute("user", user);
        model.addAttribute("completedThemes", completedThemes.size());
        model.addAttribute("totalThemes", totalThemes);
        model.addAttribute("progress", progress);
        model.addAttribute("themeService", userProgressService);
        return "themes";
    }

    @GetMapping("/themes/{id}")
    public String viewTheme(@PathVariable Long id, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            return "redirect:/login";
        }
        if (!userProgressService.checkIfUserCanAccessTheme(user.getId(), id)) {
            return "redirect:/themes";
        }
        Theme theme = themeService.findById(id);
        Test test = theme.getTest();
        UserProgress progress = userProgressRepository.findByUserAndTheme(user, theme);
        boolean isCompleted= false;
        if(progress != null){
            isCompleted = progress.getIsCompleted();
        }
        boolean nextThemeExists = themeService.existsById(id + 1);
        boolean testPassed = testService.isTestPassed(user.getId(), test.getTestId());
        if (theme != null) {
            model.addAttribute("theme", theme);
            model.addAttribute("testPassed", testPassed);
            model.addAttribute("isCompleted", isCompleted);
            model.addAttribute("nextThemeExists", nextThemeExists);
            return "theme-detail";
        } else {
            return "redirect:/themes";
        }
    }
    @PostMapping("/themes/{themeId}/complete")
    public String completeTheme(@PathVariable Long themeId, Authentication authentication, RedirectAttributes redirectAttributes) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            return "redirect:/login";
        }

        boolean success = userProgressService.markThemeAsCompleted(user.getId(), themeId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Тема успешно завершена!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при завершении темы.");
        }
        return "redirect:/themes/{themeId}";
    }

}

package net.guides.springboot.registrationlogindemo.service;

import net.guides.springboot.registrationlogindemo.entity.Theme;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.entity.UserProgress;
import net.guides.springboot.registrationlogindemo.repository.ThemeRepository;
import net.guides.springboot.registrationlogindemo.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserProgressRepository userProgressRepository;

    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id).orElse(null);
    }
    public String calculateProgressPercentage(User user) {
        List<UserProgress> completedThemes = userProgressRepository.findByUserAndIsCompleted(user, true);
        int totalThemes = (int) themeRepository.count();
        return completedThemes.size() + " из " + totalThemes;
    }

    public boolean checkAllThemesCompleted(User user) {
        List<UserProgress> completedThemes = userProgressRepository.findByUserAndIsCompleted(user, true);
        int totalThemes = (int) themeRepository.count();
        return completedThemes.size() == totalThemes;
    }

    public int countAllThemes() {
        return (int) themeRepository.count();
    }

    public boolean existsById(Long id) {
        return themeRepository.existsById(id);
    }
}

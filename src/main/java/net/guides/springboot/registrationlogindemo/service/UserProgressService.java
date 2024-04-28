package net.guides.springboot.registrationlogindemo.service;

import net.guides.springboot.registrationlogindemo.entity.Theme;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.entity.UserProgress;
import net.guides.springboot.registrationlogindemo.repository.ThemeRepository;
import net.guides.springboot.registrationlogindemo.repository.UserProgressRepository;
import net.guides.springboot.registrationlogindemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProgressService {
    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThemeRepository themeRepository;

    public boolean checkIfUserCanAccessTheme(Long userId, Long themeId) {
        if (themeId == 1) { // Предполагаем, что первая тема всегда доступна
            return true;
        }
        User user = userRepository.findById(userId).orElse(null);
        Theme previousTheme = themeRepository.findById(themeId - 1).orElse(null);
        if (user == null || previousTheme == null) {
            return false;
        }
        UserProgress progress = userProgressRepository.findByUserAndTheme(user, previousTheme);
        return progress != null && progress.getIsCompleted();
    }



    public boolean markThemeAsCompleted(Long userId, Long themeId) {
        User user = userRepository.findById(userId).orElse(null);
        Theme theme = themeRepository.findById(themeId).orElse(null);
        if (user != null && theme != null) {
            UserProgress progress = new UserProgress();
            progress.setUser(user);
            progress.setTheme(theme);
            progress.setIsCompleted(true);
            userProgressRepository.save(progress);
            return true;
        }
        return false;
    }

    public boolean checkThemeIsCompleted(User user,Theme theme){
        UserProgress progress = userProgressRepository.findByUserAndTheme(user, theme);
        boolean isCompleted= false;
        if(progress != null){
            return isCompleted = progress.getIsCompleted();
        }
        return false;
    }

    public List<UserProgress> findCompletedThemesByUser(User user) {
        return userProgressRepository.findByUserAndIsCompleted(user, true);
    }



}

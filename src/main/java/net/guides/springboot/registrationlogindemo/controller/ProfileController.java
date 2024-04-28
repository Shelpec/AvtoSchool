package net.guides.springboot.registrationlogindemo.controller;

import ch.qos.logback.classic.Logger;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.security.CustomUserDetails;
import net.guides.springboot.registrationlogindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = userService.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        model.addAttribute("user", user);
        return "profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails currentUser,
                                @RequestParam("name") String name,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(currentUser.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
            userService.updateUserProfile(user, name);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating the profile.");
        }
        return "redirect:/profile";
    }
    @PostMapping("/profile/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails currentUser,
                                 @RequestParam("password") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "Passwords do not match.");
            return "redirect:/profile";
        }

        User user = userService.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String encodedPassword = passwordEncoder.encode(newPassword);
        userService.changeUserPassword(user, encodedPassword);

        redirectAttributes.addFlashAttribute("passwordSuccess", "Password changed successfully.");
        return "redirect:/profile";
    }

}

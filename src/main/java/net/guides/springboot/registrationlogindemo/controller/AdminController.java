package net.guides.springboot.registrationlogindemo.controller;

import lombok.AllArgsConstructor;
import net.guides.springboot.registrationlogindemo.dto.MessageResponse;
import net.guides.springboot.registrationlogindemo.dto.UserDto;
import net.guides.springboot.registrationlogindemo.entity.Role;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.repository.RoleRepository;
import net.guides.springboot.registrationlogindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class AdminController {
    private UserService userService;
    private RoleRepository roleRepository;

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = (User) userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Не удалось найти пользователя с ID " + id));

        List<Role> roles = roleRepository.findAll();
        List<Long> userRoleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        model.addAttribute("userRoleIds", userRoleIds);
        return "edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUserRoles(@PathVariable("id") Long id, @RequestParam("roles") List<Long> rolesIds) {
        userService.updateUserRoles(id, rolesIds);
        return "redirect:/users";
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }
}

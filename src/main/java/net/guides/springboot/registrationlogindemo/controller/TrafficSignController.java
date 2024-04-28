package net.guides.springboot.registrationlogindemo.controller;

import net.guides.springboot.registrationlogindemo.entity.TrafficSign;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.service.TrafficSignService;
import net.guides.springboot.registrationlogindemo.service.UserProgressService;
import net.guides.springboot.registrationlogindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class TrafficSignController {
    @Autowired
    private TrafficSignService service;

    @Autowired
    private UserService userService;

    @Autowired
     private UserProgressService userProgressService;

    @GetMapping("/traffic-sign")
    public String showAllSigns(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        String userName = user.getName();
        model.addAttribute("signs", service.findAll());
        model.addAttribute("userName", userName);
        return "traffic-sign";
    }

    @GetMapping("/add")
    public String addSignForm(Model model) {
        TrafficSign sign = new TrafficSign();
        model.addAttribute("sign", sign);
        return "add_sign";
    }

    @PostMapping("/save")
    public String saveSign(@ModelAttribute("sign") TrafficSign sign) {
        service.save(sign);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteSign(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/";
    }
}

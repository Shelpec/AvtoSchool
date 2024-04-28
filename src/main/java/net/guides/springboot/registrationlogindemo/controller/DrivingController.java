package net.guides.springboot.registrationlogindemo.controller;

import net.guides.springboot.registrationlogindemo.entity.DrivingSession;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.service.DrivingService;
import net.guides.springboot.registrationlogindemo.service.ThemeService;
import net.guides.springboot.registrationlogindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class DrivingController {

    @Autowired
    private DrivingService drivingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ThemeService themeService;

    @GetMapping("/drivers")
    public String listAllDrivers(Model model, Principal principal) {
        if (drivingService.alreadyHasSession(principal.getName())) {
            return "redirect:/my-sessions";
        }
        User user = userService.findUserByEmail(principal.getName());
        boolean allThemesCompleted = themeService.checkAllThemesCompleted(user);
        model.addAttribute("drivers", userService.findAllDrivers());
        model.addAttribute("allThemesCompleted", allThemesCompleted);
        return "drivers-list";
    }


    @GetMapping("/book/{driverId}")
    public String showBookingForm(@PathVariable Long driverId, Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        boolean allThemesCompleted = themeService.checkAllThemesCompleted(user);
        if(!allThemesCompleted){
            return "redirect:/index";
        }
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<String> availableSlots = drivingService.generateTimeSlots(tomorrow, driverId);
        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("driverId", driverId);
        model.addAttribute("day", tomorrow);
        return "book-driving-session";
    }


    // Обработка бронирования
    @PostMapping("/book")
    public String bookDrivingSession(@RequestParam("driverId") Long driverId,
                                     @RequestParam("sessionTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime sessionTime,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
        LocalDate tomorrow = LocalDate.now().plusDays(1); // Используйте текущую дату или любую другую дату по вашему выбору
        LocalDateTime fullSessionTime = LocalDateTime.of(tomorrow, sessionTime); // Соедините дату и время

        User driver = userService.findUserById(driverId);
        User student = userService.findUserByEmail(principal.getName());

        boolean allThemesCompleted = themeService.checkAllThemesCompleted(student);
        if(!allThemesCompleted){
            return "redirect:/index";
        }

        if (driver == null || student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Driver or student not found!");
            return "redirect:/drivers";
        }

        if (drivingService.sessionExists(student.getId(), driver.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have already booked a session with this driver.");
            return "redirect:/drivers";
        }

        if (!drivingService.isValidSessionTime(fullSessionTime)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid session time. Bookings are allowed on weekdays from 9 AM to 5 PM.");
            return "redirect:/book/" + driverId;
        }

        DrivingSession session = new DrivingSession();
        session.setDriver(driver);
        session.setStudent(student);
        session.setSessionTime(fullSessionTime);
        drivingService.bookSession(session);

        redirectAttributes.addFlashAttribute("successMessage", "Session successfully booked!");
        return "redirect:/my-sessions";
    }



    @GetMapping("/sessions")
    public String viewSessionsForDriver(Principal principal, Model model) {
        User driver = userService.findUserByEmail(principal.getName());
        Long driverId = driver.getId();
        model.addAttribute("sessions", drivingService.getSessionsForDriver(driverId));
        return "driver-sessions";
    }

    @GetMapping("/my-sessions")
    public String viewMySessions(Principal principal, Model model) {
        User student = userService.findUserByEmail(principal.getName());
        boolean allThemesCompleted = themeService.checkAllThemesCompleted(student);
        if(!allThemesCompleted){
            return "redirect:/index";
        }
        Long studentId= student.getId();
        model.addAttribute("stud_sessions", drivingService.getSessionsForStudent(studentId));
        return "my-sessions";
    }
}


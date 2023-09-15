package com.example.margocoursework.Controllers;

import com.example.margocoursework.Model.Client;
import com.example.margocoursework.Model.User;
import com.example.margocoursework.Repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @ModelAttribute(name = "user")
    public User user(){return new Client();}
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    public RegistrationController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }
    @GetMapping
    public String getRegister(){ return "registration"; }
    @PostMapping
    public String processRegistration(@Valid User user, Errors errors, Model model) {
        if(errors.hasErrors())
            return "registration";
        user.setPassword(encoder.encode(user.getPassword()));
        if(userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("exists", "Пользователь с таким логином уже зарегистрирован!");
            return "login";
        }
        userRepository.save(user);
        return "redirect:/login";
    }
}

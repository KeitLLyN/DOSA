package com.security.university.controllers;

import com.security.university.entity.Role;
import com.security.university.entity.User;
import com.security.university.entity.dto.CaptchaResponseDto;
import com.security.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserRepository USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final RestTemplate REST_TEMPLATE;

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.USER_REPOSITORY = userRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
        this.REST_TEMPLATE = restTemplate;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          User user,
                          Model model) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = REST_TEMPLATE.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (response != null && !response.isSuccess()) {
            model.addAttribute("captchaError", "Fill the captcha");
            return "registration";
        }
        User userFromDB = USER_REPOSITORY.findByUsername(user.getUsername());
        if (userFromDB != null) {
            model.addAttribute("isExist", true);
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        USER_REPOSITORY.save(user);
        return "redirect:/login";
    }
}

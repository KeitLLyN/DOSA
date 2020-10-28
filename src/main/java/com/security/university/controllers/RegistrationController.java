package com.security.university.controllers;

import com.security.university.entity.User;
import com.security.university.entity.dto.CaptchaResponseDto;
import com.security.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserService USER_SERVICE;

    private final RestTemplate REST_TEMPLATE;

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @Value("${password.length.min}")
    private int passwordMinLength;

    @Value("${username.length.max}")
    private int usernameMaxLength;

    @Value("${username.length.min}")
    private int usernameMinLength;

    @Autowired
    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.USER_SERVICE = userService;
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
            model.addAttribute("captchaError", true);
            return "registration";
        }
        if (user.getUsername().length() < usernameMinLength || user.getUsername().length() > usernameMaxLength){
            model.addAttribute("usernameWrongLength", true);
            return "registration";
        }
        if (user.getPassword().length() < passwordMinLength) {
            model.addAttribute("passwordWrongLength", true);
            return "registration";
        }
        if (!USER_SERVICE.addUser(user)) {
            model.addAttribute("isExist", true);
            return "registration";
        }
        return "redirect:/";
    }

    @ModelAttribute("usernameMin")
    private int getUsernameMinLength(){
        return usernameMinLength;
    }

    @ModelAttribute("usernameMax")
    private int getUsernameMaxLength(){
        return usernameMaxLength;
    }

    @ModelAttribute("passwordMin")
    private int getPasswordMinLength(){
        return passwordMinLength;
    }
}

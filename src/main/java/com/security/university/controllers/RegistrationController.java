package com.security.university.controllers;

import com.security.university.entity.User;
import com.security.university.entity.dto.CaptchaResponseDto;
import com.security.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserService USER_SERVICE;

    private final RestTemplate REST_TEMPLATE;

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.USER_SERVICE = userService;
        this.REST_TEMPLATE = restTemplate;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = REST_TEMPLATE.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (response != null && !response.isSuccess()) {
            model.addAttribute("captchaError", true);
            return "registration";
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!USER_SERVICE.addUser(user)) {
            model.addAttribute("isExist", true);
            return "registration";
        }
        return "redirect:/";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        boolean isActivated = USER_SERVICE.activateUser(code);
        if (isActivated) {
            model.addAttribute("activateMessage", "User successfully activated");
        } else {
            model.addAttribute("activateMessage", "Activation code is not found!");
        }
        return "login";
    }
}

package com.security.university.controllers;

import com.security.university.entity.Role;
import com.security.university.entity.User;
import com.security.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final UserRepository USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Autowired
    public RegistrationController(UserRepository USER_REPOSITORY, PasswordEncoder PASSWORD_ENCODER) {
        this.USER_REPOSITORY = USER_REPOSITORY;
        this.PASSWORD_ENCODER = PASSWORD_ENCODER;
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDB = USER_REPOSITORY.findByUsername(user.getUsername());
        if (userFromDB != null){
            model.put("isExist",true);
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        USER_REPOSITORY.save(user);
        return "redirect:/login";
    }
}

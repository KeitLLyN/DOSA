package com.security.university.controllers;

import com.security.university.entity.User;
import com.security.university.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserRepository USER_REPOSITORY;

    public  UserController(UserRepository userRepository){
        this.USER_REPOSITORY = userRepository;
    }

    @GetMapping
    public String userList(Model model){
        Iterable<User> users = USER_REPOSITORY.findAll();
        model.addAttribute("users",users);
        return "userList";
    }
}

package com.security.university.controllers;

import com.security.university.entity.Message;
import com.security.university.entity.Role;
import com.security.university.entity.User;
import com.security.university.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final MessageRepository MESSAGE_REPOSITORY;

    @Autowired
    public MainController(MessageRepository messageRepository) {
        this.MESSAGE_REPOSITORY = messageRepository;
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "") String filter,
                       Model model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = MESSAGE_REPOSITORY.findByTag(filter);
        } else {
            messages = MESSAGE_REPOSITORY.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag,
                      Model model) {
        Message message = new Message(text, tag, user);
        MESSAGE_REPOSITORY.save(message);
        Iterable<Message> messages = MESSAGE_REPOSITORY.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String message_id,
                         @RequestParam String user_id,
                         @AuthenticationPrincipal User user) {
        if (user_id.equals(user.getId().toString()) || user.getRoles().contains(Role.ADMIN)) {
            MESSAGE_REPOSITORY.deleteById(Long.parseLong(message_id));
        }
        return "main";
    }
}

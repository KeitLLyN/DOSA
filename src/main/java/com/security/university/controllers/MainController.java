package com.security.university.controllers;

import com.security.university.entity.Message;
import com.security.university.entity.Role;
import com.security.university.entity.User;
import com.security.university.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("isAdmin", user.isAdmin());
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @ModelAttribute Message message) {
        message.setAuthor(user);
        MESSAGE_REPOSITORY.save(message);
        return "redirect:/main";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         @RequestParam Long user_id,
                         @AuthenticationPrincipal User user) {
        if (user_id.equals(user.getId()) || user.getRoles().contains(Role.ADMIN)) {
            MESSAGE_REPOSITORY.deleteById(id);
        }
        return "redirect:/main";
    }

    @ModelAttribute("message")
    public Message message(){
        return new Message();
    }
}

package com.security.university.controllers;


import com.security.university.hash.CustomHashAlgorithms;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashController {

    @GetMapping("/hash")
    public String get(Model model) {
        model.addAttribute("encodedText", "Ваше зашифрованое сообщение");
        return "hash";
    }

    @PostMapping("/hash")
    public String hash(@RequestParam("hashText") String hashText,
                       @RequestParam("algorithm") String algorithm,
                       Model model) {
        model.addAttribute("encodedText", CustomHashAlgorithms.customAlgorithm(hashText, algorithm));
        return "hash";
    }
}

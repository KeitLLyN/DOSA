package com.security.university.service;

import com.security.university.entity.Role;
import com.security.university.entity.User;
import com.security.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;
    private final MailSender MAIL_SENDER;

    @Value("${web-site.url}")
    private String siteUrl;

    @Autowired
    public UserService(UserRepository USER_REPOSITORY, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.USER_REPOSITORY = USER_REPOSITORY;
        this.PASSWORD_ENCODER = passwordEncoder;
        this.MAIL_SENDER = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return USER_REPOSITORY.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDB = USER_REPOSITORY.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());

        USER_REPOSITORY.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s!\nPlease, visit next link: %s/activate/%s",
                    user.getUsername(), siteUrl, user.getActivationCode());
            MAIL_SENDER.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = USER_REPOSITORY.findByActivationCode(code);
        if (user == null)
            return false;
        user.setActivationCode(null);
        USER_REPOSITORY.save(user);
        return true;
    }
}

package com.security.university.config;

import com.security.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService USER_SERVICE;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Value("${security.require-ssl}")
    private boolean requireHttps;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.USER_SERVICE = userService;
        this.PASSWORD_ENCODER = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (requireHttps){
            http
                    .authorizeRequests()
                    .antMatchers("/", "/registration", "/activate/*").permitAll()
                    .anyRequest().authenticated()
                    .and()
                        .formLogin()
                        .loginPage("/login")
                        .permitAll()
                    .and()
                        .logout()
                        .permitAll()
                    .and()
                        .requiresChannel()
                        .anyRequest()
                        .requiresSecure();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(USER_SERVICE)
                .passwordEncoder(PASSWORD_ENCODER);
    }
}

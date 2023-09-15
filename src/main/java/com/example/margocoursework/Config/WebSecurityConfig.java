package com.example.margocoursework.Config;

import com.example.margocoursework.Model.User;
import com.example.margocoursework.Repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo){
        return username -> {
            User user = userRepo.findByUsername(username);

            if(user != null) return user;

            throw new UsernameNotFoundException("User ‘" + username + "’ not found");
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/administration/**").hasRole("ADMIN")
                    .requestMatchers("/profile").authenticated()
                    .requestMatchers("/**", "/**/**").permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .logout()
                .and()
                    .build();

    }
}

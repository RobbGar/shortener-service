package com.robb.shortner.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/error/**").authenticated()
                .requestMatchers("/r/**").permitAll()
            )
            .oauth2Login(Customizer.withDefaults());

        http.csrf((conf) -> conf.disable());
        return http.build();
    }
}
package com.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
//debbug the security method
@EnableWebSecurity(debug = true)
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        security.authorizeHttpRequests(request -> request
                        // USER APIs
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "NORMAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // PRODUCT APIs
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/api/products/**").hasRole("ADMIN")  // Applies to POST, PUT, DELETE etc.

                        // CATEGORY APIs
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

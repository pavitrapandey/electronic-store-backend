package com.electronic.store.config;

import com.electronic.store.Security.JwtAuthenticationEntryPoints;
import com.electronic.store.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
//debbug the security method
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private JwtAuthenticationEntryPoints entryPoints;

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
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        //API AUTHENTICATION
                        // AUTH APIs - Only POST for token generation is permitted
                        .requestMatchers(HttpMethod.POST, "/api/auth/generate-token","/api/auth/regenerate-token").permitAll()
                        .requestMatchers("/api/auth/**").authenticated()

                        .anyRequest().authenticated()
                );

//        security.httpBasic(Customizer.withDefaults());
        security.exceptionHandling(ex->
                ex.authenticationEntryPoint(entryPoints));

        security.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

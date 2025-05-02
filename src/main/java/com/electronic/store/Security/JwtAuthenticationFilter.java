package com.electronic.store.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //api se phle chlega Jwt ko verifiy krne ke liye
        String requestHeader =request.getHeader("Authorization");

        String username = null;
        String token = null;

        //Authorization: Bearer token
        if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try {
                username = helper.getUsernameFromToken(token);
            }
            catch (IllegalArgumentException e) {
                logger.info("illegal argument");

            }
            catch (ExpiredJwtException e) {
                logger.info("token expired");
            }
            catch (Exception e) {
                logger.info("token is not valid");
            }


        }else {
            logger.warn("JWT token does not begin with Bearer String");
        }

        //if username is not null and security context is empty
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
            //if token is valid configure spring security to manually set authentication
            if(username.equals(userDetails.getUsername()) && !helper.isTokenExpired(token)){
                //token valid
                //security context me authentication set krna h
               UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        filterChain.doFilter(request,response);

    }
}

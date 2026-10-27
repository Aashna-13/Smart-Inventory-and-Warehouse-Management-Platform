package com.smartinventory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
 private final JwtAuthFilter jwtFilter;
 public SecurityConfig(JwtAuthFilter jwtFilter){this.jwtFilter=jwtFilter;}
 @Bean SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
  return http.csrf(csrf->csrf.disable())
   .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/","/index.html","/login.html","/css/**","/js/**","/favicon.ico","/actuator/health","/api/auth/login").permitAll()
    .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN","WAREHOUSE_MANAGER")
    .requestMatchers(org.springframework.http.HttpMethod.POST,"/api/auth/register").hasRole("ADMIN")
    .requestMatchers(org.springframework.http.HttpMethod.POST,"/api/**").hasAnyRole("ADMIN","WAREHOUSE_MANAGER")
    .requestMatchers(org.springframework.http.HttpMethod.PUT,"/api/**").hasAnyRole("ADMIN","WAREHOUSE_MANAGER")
    .requestMatchers(org.springframework.http.HttpMethod.DELETE,"/api/**").hasRole("ADMIN")
    .requestMatchers("/api/**").authenticated().anyRequest().permitAll())
   .exceptionHandling(e->e.authenticationEntryPoint((req,res,ex)->res.sendError(401,"Unauthorized"))
    .accessDeniedHandler((req,res,ex)->res.sendError(403,"Forbidden")))
   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();
 }
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception{return c.getAuthenticationManager();}
}

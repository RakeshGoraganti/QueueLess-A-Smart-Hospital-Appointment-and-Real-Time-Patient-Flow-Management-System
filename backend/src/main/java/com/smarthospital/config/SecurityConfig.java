package com.smarthospital.config;
import com.smarthospital.security.JwtFilter; import org.springframework.context.annotation.*; import org.springframework.security.config.Customizer; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; import org.springframework.web.cors.*;
import java.util.List;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtFilter jwt)throws Exception{
  http.csrf(c->c.disable()).cors(Customizer.withDefaults()).authorizeHttpRequests(a->a
   .requestMatchers("/api/auth/**","/api/hospitals/**","/api/departments/**","/api/doctors","/swagger-ui/**","/v3/api-docs/**").permitAll()
   .requestMatchers("/api/admin/**","/api/dashboard/stats").hasRole("ADMIN")
   .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
   .requestMatchers("/api/queue/**").authenticated()
   .requestMatchers("/api/appointments/**").authenticated()
   .anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class);
  return http.build();
 }
 @Bean CorsConfigurationSource corsConfigurationSource(){CorsConfiguration c=new CorsConfiguration();c.setAllowedOrigins(List.of("http://localhost:5173","http://127.0.0.1:5173"));c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("*"));c.setAllowCredentials(true);UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s;}
}

package com.smarthospital.controller;
import com.smarthospital.model.AppUser; import com.smarthospital.repo.UserRepository; import com.smarthospital.security.JwtService; import jakarta.validation.constraints.*; import org.springframework.http.*; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public record RegisterRequest(@NotBlank String name,@Email @NotBlank String email,@Size(min=8) String password,String phone){}
 public record LoginRequest(@Email @NotBlank String email,@NotBlank String password){}
 @PostMapping("/register") public ResponseEntity<?> register(@RequestBody RegisterRequest r){
  if(users.existsByEmail(r.email().toLowerCase()))return ResponseEntity.badRequest().body(Map.of("message","Email already registered"));
  AppUser u=new AppUser(r.name(),r.email().toLowerCase(),encoder.encode(r.password()),"PATIENT");u.phone=r.phone();users.save(u);return ResponseEntity.status(201).body(Map.of("message","Registration successful"));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody LoginRequest r){
  AppUser u=users.findByEmail(r.email().toLowerCase()).orElse(null);
  if(u==null||!u.active||!encoder.matches(r.password(),u.password))return ResponseEntity.status(401).body(Map.of("message","Invalid email or password"));
  return ResponseEntity.ok(Map.of("token",jwt.generate(u.email,u.role),"role",u.role,"name",u.name,"email",u.email,"id",u.id));
 }
}

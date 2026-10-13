package com.smarthospital.security;
import com.smarthospital.repo.UserRepository; import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter; import java.io.IOException; import java.util.List;
@Component
public class JwtFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final UserRepository users;
 public JwtFilter(JwtService jwt,UserRepository users){this.jwt=jwt;this.users=users;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String h=req.getHeader("Authorization");
if(h!=null&&h.startsWith("Bearer ")){String t=h.substring(7);try{if(jwt.valid(t)){var u=users.findByEmail(jwt.email(t)).orElse(null);if(u!=null&&u.active){var a=new UsernamePasswordAuthenticationToken(u.getEmail(),null,List.of(new SimpleGrantedAuthority("ROLE_"+u.role)));SecurityContextHolder.getContext().setAuthentication(a);}}}catch(Exception ignored){}}
chain.doFilter(req,res);
 }
}

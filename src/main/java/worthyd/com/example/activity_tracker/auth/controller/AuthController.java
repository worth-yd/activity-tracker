package worthyd.com.example.activity_tracker.auth.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import worthyd.com.example.activity_tracker.auth.config.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password));

    if (auth.isAuthenticated()) {
      return jwtUtil.generateToken(username);
    } else {
      throw new RuntimeException("Invalid login");
    }
  }
}


package worthyd.com.example.activity_tracker.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import worthyd.com.example.activity_tracker.auth.config.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  private static final String GOOGLE_CLIENT_ID = "774002077039-8bkm16b6i18pvf8uosskmfaorkfaidnh.apps.googleusercontent.com";

  public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  // 🔹 Normal login (email ile)
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
    try {
      String email = request.get("email"); // artık email
      String password = request.get("password");

      Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, password) // email bazlı auth
      );

      if (auth.isAuthenticated()) {
        String token = jwtUtil.generateToken(email);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("Strict")
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of("message", "Login successful", "email", email));
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Invalid credentials"));
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Invalid email or password"));
    }
  }

  // 🔹 Google login
  @PostMapping("/google")
  public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
    String idTokenString = body.get("token");

    try {
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
          new NetHttpTransport(),
          new com.google.api.client.json.gson.GsonFactory())
          .setAudience(java.util.Collections.singletonList(GOOGLE_CLIENT_ID))
          .build();

      GoogleIdToken idToken = verifier.verify(idTokenString);
      if (idToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Invalid Google token"));
      }

      GoogleIdToken.Payload payload = idToken.getPayload();
      String email = payload.getEmail();
      String name = (String) payload.get("name");

      // ⚡ Burada kullanıcıyı DB’de oluştur / güncelle

      String token = jwtUtil.generateToken(email);

      ResponseCookie cookie = ResponseCookie.from("jwt", token)
          .httpOnly(true)
          .secure(false)
          .path("/")
          .maxAge(7 * 24 * 60 * 60)
          .sameSite("Strict")
          .build();

      return ResponseEntity.ok()
          .header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body(Map.of("message", "Google login successful", "email", email, "name", name));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Google login failed"));
    }
  }

  // 🔹 Logout
  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    ResponseCookie cookie = ResponseCookie.from("jwt", "")
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(0)
        .sameSite("Strict")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(Map.of("message", "Logged out successfully"));
  }
}

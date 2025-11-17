package worthyd.com.example.activity_tracker.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  private final String SECRET_KEY = "mySecretKeymySecretKeymySecretKey"; // 256-bit
  private final long EXPIRATION = 1000 * 60 * 60; // 1 saat

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  // JWT oluştururken artık email kullanıyoruz
  public String generateToken(String email) {
    return Jwts.builder()
        .setSubject(email) // subject = email
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  // Token’dan email’i al
  public String extractEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }
}


package worthyd.com.example.activity_tracker.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsService userService;

  public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();
    // login, register ve public endpoint'leri atla
    if (path.startsWith("/auth/login") || path.startsWith("/auth/register")|| path.startsWith("/auth/google")) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwt = null;
    String Email = null;

    // ✅ 1. Token'ı Cookie'den oku
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("jwt".equals(cookie.getName())) {
          jwt = cookie.getValue();
          break;
        }
      }
    }

    // ✅ 2. Cookie bulunamadıysa, fallback olarak Authorization header'ı dene
    // (opsiyonel)
    if (jwt == null) {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        jwt = authHeader.substring(7);
      }
    }

    // ✅ 3. Token varsa kullanıcıyı doğrula
    if (jwt != null) {
      Email = jwtUtil.extractEmail(jwt);


      if (Email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userService.loadUserByUsername(Email);

        if (jwtUtil.validateToken(jwt)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}

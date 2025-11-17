package worthyd.com.example.activity_tracker.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import worthyd.com.example.activity_tracker.auth.repository.*;
import worthyd.com.example.activity_tracker.auth.model.*;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // AuthenticationManager burada çağırır
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // UserEntity → UserDetails
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        new ArrayList<>());
  }
}

package com.schedule.schedulekyg.service.user;

import com.schedule.schedulekyg.repository.UserRepository;
import com.schedule.schedulekyg.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    final UserVO userInfo = this.userRepository.getUserInfo(username);

    if(!Objects.isNull(userInfo)) {
      return User.builder()
              .username(userInfo.getUserName())
              .password(userInfo.getUserPassword())
              .roles("ADMIN")
              .build();
    }

    return null;
  }

}

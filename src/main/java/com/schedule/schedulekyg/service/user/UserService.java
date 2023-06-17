package com.schedule.schedulekyg.service.user;

import com.schedule.schedulekyg.repository.UserRepository;
import com.schedule.schedulekyg.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserVO userInfo = this.userRepository.getUserInfo();
    log.info("userInfo :::: "+ userInfo.toString());

    log.info("loadUserByUsername ::: " + username);



    return null;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}

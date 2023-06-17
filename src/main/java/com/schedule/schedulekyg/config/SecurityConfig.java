package com.schedule.schedulekyg.config;

import com.schedule.schedulekyg.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig {

  @Autowired
  private UserService userService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .headers().frameOptions().disable()
      .and()
        .authorizeRequests()
        .antMatchers("/"
                , "/vendor/**"
                , "/scss/**"
                , "/css/**"
                , "/img/**"
                , "/js/**").permitAll()
//            .antMatchers("/api/v1/**").hasRole(Role.
//                    USER.name())
        .anyRequest().authenticated()
      .and()
        .logout()
        .logoutSuccessUrl("/");
    return http.build();
  }

  // 인증 담당
  @Bean
  AuthenticationManager authenticationManager(
          AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}

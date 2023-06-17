package com.schedule.schedulekyg.config;

import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .headers().frameOptions().disable()
      .and()
        .authorizeRequests()
        .antMatchers("/login"
                , "/vendor/**"
                , "/scss/**"
                , "/css/**"
                , "/img/**"
                , "/js/**").permitAll()
//            .antMatchers("/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/loginProc")
        .defaultSuccessUrl("/")
      .and()
        .logout()
        .logoutSuccessUrl("/login");
    return http.build();
  }

  // 인증 담당
//  @Bean
//  AuthenticationManager authenticationManager(
//          AuthenticationConfiguration authenticationConfiguration) throws Exception {
//    return authenticationConfiguration.getAuthenticationManager();
//  }

}

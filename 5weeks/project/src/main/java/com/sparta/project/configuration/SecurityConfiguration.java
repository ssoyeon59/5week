package com.sparta.project.configuration;


import com.sparta.project.jwt.AccessDeniedHandlerException;
import com.sparta.project.jwt.AuthenticationEntryPointException;
import com.sparta.project.jwt.TokenProvider;
import com.sparta.project.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

  @Value("${jwt.secret}")
  String SECRET_KEY;
  private final TokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationEntryPointException authenticationEntryPointException;
  private final AccessDeniedHandlerException accessDeniedHandlerException;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
    return (web) -> web.ignoring() //웹 시큐린티를 제외하고 로그인을 무시하고 실행하게 해주겠다
            .antMatchers("/h2-console/**");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.cors();

      http.csrf().disable()

          .exceptionHandling()
          .authenticationEntryPoint(authenticationEntryPointException)
          .accessDeniedHandler(accessDeniedHandlerException)

          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

          .and()
          .authorizeRequests()
          .antMatchers("/api/member/**").permitAll()
          .antMatchers("/api/post/**").permitAll()
          .antMatchers("/api/comment/**").permitAll()
          .antMatchers( "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**").permitAll()
          .anyRequest().authenticated()

          .and()
          .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService));

      return http.build();
  }
}

package com.side.football_project.global.config;

import com.side.football_project.global.security.auth.DelegatedAccessDeniedHandler;
import com.side.football_project.global.security.auth.DelegatedAuthenticationEntryPoint;
import com.side.football_project.global.security.filter.AuthFilter;
import com.side.football_project.global.util.UrlUtil;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DelegatedAccessDeniedHandler delegatedAccessDeniedHandler;
    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;
    private final AuthFilter authFilter;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // AuthenticationProvider 빈 제거 - AuthFilter에서 직접 처리


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .headers(HeadersConfigurer -> HeadersConfigurer.frameOptions(org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(UrlUtil.WHITE_LIST_PATHS).permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/static/**").permitAll()
                                .requestMatchers("*.html", "*.css", "*.js", "*.png", "*.jpg", "*.ico").permitAll()
                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()
                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/teams", "/matches", "/shorts").permitAll()
                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/teams/**", "/teams/list").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/vendor/**").hasRole("VENDOR")
                                .anyRequest().authenticated()
                        )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(delegatedAuthenticationEntryPoint)
                        .accessDeniedHandler(delegatedAccessDeniedHandler)
                )

                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Logout Completed");
                        })
                );

        return http.build();
    }
}

package com.gymbuddy.auth.config;

import com.gymbuddy.auth.persistence.repository.UserRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] FILTERED_URLS = {
            "/users/**",
            "/auth/change-password"
    };

    private static final String[] UNFILTERED_URLS = {
            "/auth/**",
            "/swagger-ui/**",
            "/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity,
                                                   final JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return httpSecurity.csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .cors()
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS, "**").permitAll()
                .requestMatchers(UNFILTERED_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(UNFILTERED_URLS);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> logFilter(final UserRepository userRepository) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(userRepository));
        registrationBean.addUrlPatterns(FILTERED_URLS);
        return registrationBean;
    }

    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        corsConfig.setAllowCredentials(false);
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addExposedHeader("");
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }

    // dummy implementation to suppress some default Spring Security configuration
    @Bean
    public UserDetailsService dummyUserDetailsService() {
        return username -> null;
    }
}

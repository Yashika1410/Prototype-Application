package com.crafters.DataService.config;

import com.crafters.DataService.services.Impl.UserServiceImpl;
import com.crafters.DataService.utils.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configuration class for defining the security settings of the application.
 */
@Configuration
public class WebSecurityConfig {

    private final UserServiceImpl userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructs a WebSecurityConfig with the necessary dependencies.
     *
     * @param userService             The user service implementation.
     * @param jwtAuthenticationFilter The JWT authentication filter.
     */
    public WebSecurityConfig(UserServiceImpl userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the security filter chain for the application.
     *
     * @param httpSecurity The HttpSecurity object to configure.
     * @return A SecurityFilterChain configured for the application.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api-docs/**",
                                "/swagger-ui/**"
                        )
                        .permitAll()
                        .requestMatchers("/api/v1/user/**")
                        .hasAuthority("USER")
                        .requestMatchers("/api/v1/admin/**")
                        .hasAuthority("ADMIN").anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * Configures the password encoder bean.
     *
     * @return A PasswordEncoder bean using BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider bean.
     *
     * @return An AuthenticationProvider bean using DaoAuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the authentication manager bean.
     *
     * @param config The AuthenticationConfiguration to get the authentication manager.
     * @return An AuthenticationManager bean.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

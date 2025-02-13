package bootcamp.petclinic.config;

import bootcamp.petclinic.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CorsConfig corsConfig;
    private final LogoutHandler logoutHandler;

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/logout", "/error"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/pets/{petId}").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/pets/user/{userId}").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/pets/add").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/pets/{petId}").hasAnyRole("USER", "ADMIN", "DOCTOR")

                        .requestMatchers("/api/v1/visits/{visitId}/status").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/visits/all").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/v1/visits/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/schedule/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/availability/**").hasAnyRole("USER", "ADMIN", "DOCTOR")

                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler((request, response, authentication) ->
                                org.springframework.security.core.context.SecurityContextHolder.clearContext())
                )
                .build();
    }
}

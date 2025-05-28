package dj.nwp.sofar.config;

import dj.nwp.sofar.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class SecurityConfig {
    private final JWTFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    private static final String[] WHITE_LIST_URL = {
            "/auth/login",
            "/actuator/health",
            "/routes",
            "/order/track/ping/**",
            "/ws/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth


                        .requestMatchers(WHITE_LIST_URL).permitAll()

                        .requestMatchers(HttpMethod.GET,"/error/**").hasAuthority("CAN_VIEW")
                        .requestMatchers(HttpMethod.GET,"/permissions/as-string").hasAuthority("CAN_VIEW")
                        .requestMatchers(HttpMethod.GET,"/dish/**").hasAuthority("CAN_VIEW")

                        .requestMatchers(HttpMethod.GET,"/order").hasAuthority("CAN_VIEW")
                        .requestMatchers(HttpMethod.POST, "/order/schedule").hasAuthority("CAN_SCHEDULE_ORDER")
                        .requestMatchers(HttpMethod.POST, "/order/place").hasAuthority("CAN_PLACE_ORDER")
                        .requestMatchers(HttpMethod.GET,"/order/search/**").hasAuthority("CAN_SEARCH_ORDER")
                        .requestMatchers(HttpMethod.PUT, "/order/cancel/**").hasAuthority("CAN_CANCEL_ORDER")

                        .requestMatchers(HttpMethod.POST, "/dish/create").hasAuthority("CAN_CREATE")
                        .requestMatchers(HttpMethod.POST, "/dish/edit/").hasAuthority("CAN_EDIT")
                        .requestMatchers(HttpMethod.DELETE, "/dish/delete/*").hasAuthority("CAN_DELETE")

                        .requestMatchers(HttpMethod.POST, "/user").hasAuthority("CAN_CREATE")
                        .requestMatchers(HttpMethod.GET, "/user").hasAnyAuthority("CAN_VIEW")
                        .requestMatchers(HttpMethod.GET, "/user/*").hasAnyAuthority("CAN_VIEW")
                        .requestMatchers(HttpMethod.PUT, "/user/*").hasAuthority("CAN_EDIT")
                        .requestMatchers(HttpMethod.DELETE, "/user/*").hasAuthority("CAN_DELETE")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

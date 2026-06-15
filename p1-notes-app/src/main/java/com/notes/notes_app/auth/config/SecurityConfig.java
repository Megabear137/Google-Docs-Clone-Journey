package com.notes.notes_app.auth.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtProperties props) {
        SecretKey key = new SecretKeySpec(
                props.secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"
        );
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtProperties props) {
        SecretKey key = new SecretKeySpec(
                props.secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"
        );
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder decoder) throws Exception {
        return http
                // CSRF protection is unnncessary as we use header based auth
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        // Makes these endpoints publicly accessible
                        .requestMatchers("/api/auth/**").permitAll()
                        // Makes all other endpoints auth protected
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o
                        // Adds out jwtDecoder to the Security Filter Chain
                        .jwt(jwt -> jwt.decoder(decoder)))
                .build();
    }

}

package edu.carroll.initMusic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Security configuration for the application.
 * <p>
 * This class configures the security settings for the web application, including
 * authentication and authorization rules.
 * </p>
 * @author Nick Clouse
 * @author Molly O'Connor
 *
 * @since September 11, 2024
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean for password encoding using BCrypt.
     *
     * @return a BCryptPasswordEncoder instance for encoding passwords.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object to configure.
     * @return the configured SecurityFilterChain.
     * @throws Exception if an error occurs while configuring the security settings.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/images/**","/js/**").permitAll() // Allow access to static resources
                        .requestMatchers("/","/login", "/register","/loginSuccess", "/securityQuestions", "/answerSecurityQuestions",
                                "/changePasswordEmail", "/passSecurity", "/changePassword", "/emailTaken", "/userRegistered").permitAll()   // Allow access to login and registration pages
                        .requestMatchers("/","/login", "/register","/loginSuccess", "/securityQuestions", "/answerSecurityQuestions", "/changePasswordEmail", "/passSecurity", "/changePassword", "/changePasswordLoggedIn", "/passwordChangedLoggedIn").permitAll()   // Allow access to login and registration pages
                        .requestMatchers("/search","/playlists","/addSongToPlaylist","/createPlaylist","/renamePlaylist","/deletePlaylist","/viewPlaylist/**","/deleteSongFromPlaylist").permitAll()   // Allow access to login and registration pages
                        .anyRequest().authenticated()                         // Require authentication for all other requests
                )
                .logout(LogoutConfigurer::permitAll // Allow everyone to access the logout endpoint
                );

        return http.build();
    }
}


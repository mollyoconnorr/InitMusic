package edu.carroll.initMusic.config;

import edu.carroll.initMusic.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


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

    /** UserDetailsService to use */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Injects dependencies
     * @param userDetailsService UserDetailsService to inject
     */
    public SecurityConfig(final CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/login",
                        "/login?error",
                        "/register",
                        "/js/**",
                        "/css/**",
                        "/changePasswordEmail",
                        "/securityQuestions",
                        "/changePassword",
                        "/securityQuestionsUpdated",
                        "/passSecurity",
                        "/images/**").permitAll()
                .requestMatchers("/submitSecurityQuestions").authenticated()
                .anyRequest().authenticated());
        //Set login page to our own
        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/search",true)
                        .failureUrl("/login?error")
                        .permitAll()
        );
        //Set logout actions
        http.logout(lOut -> {
            lOut.invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
        });
        http.csrf().disable();

        return http.build();
    }

    /**
     * Configures a AuthenticationManager with our own userDetailsService and password encoder
     * @param http HttpSecurity object to get AuthenticationManagerBuilder class from
     * @return New AuthenticationManager object with our custom compnonents
     * @throws Exception Any Exception that might occur
     *
     * @see AuthenticationManager
     * @see HttpSecurity
     * @see CustomUserDetailsService
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        final AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);

        return authenticationManagerBuilder.build();
    }
}


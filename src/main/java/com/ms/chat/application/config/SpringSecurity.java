package com.ms.chat.application.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ms.chat.application.Filter.JwtFilter;
import com.ms.chat.application.services.UserDetailService;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailService userDetailService;



    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private  OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(auth -> auth

                .requestMatchers("/","api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/oauth2/**").permitAll()
                 .requestMatchers("/api/v1/chat/**", "api/v1/user/**").authenticated()
                .requestMatchers("/api/v1/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll())
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2LoginSuccessHandler)

                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("https://manishchatapp.vercel.app");  // Frontend URL
                    config.addAllowedOrigin("http://localhost:5173");  // Frontend URL
                    config.addAllowedOrigin("https://manishchatapp.netlify.app");
//                    config.addAllowedOriginPattern("*");
//                    config.addAllowedOrigin("https://manishchatapp.netlify.app");
                    config.addAllowedMethod("*");  // Allow all methods (GET, POST, etc.)
                    config.addAllowedHeader("*");  // Allow all headers
                    config.setAllowCredentials(true);  // Allow credentials (cookies, etc.)
                    return config;
                }))
                .build();
    }



    @Bean
    public AuthenticationProvider  authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailService);
        return provider;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }


}

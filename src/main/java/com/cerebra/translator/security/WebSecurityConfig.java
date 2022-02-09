package com.cerebra.translator.security;

import com.cerebra.translator.service.UserService;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;


    private final UserService userService;

    public WebSecurityConfig(@Autowired JwtTokenProvider jwtTokenProvider, @Autowired UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.headers().frameOptions().disable();

        // Disable CSRF (cross site request forgery)
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {

            @Override
            public void customize(CorsConfigurer<HttpSecurity> configurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(ImmutableList.of("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(ImmutableList.of("*"));
                configuration.setMaxAge(18000L);
                configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
                configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                configurer.configurationSource(source);
            }
        }).csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();


        http.addFilterBefore(
                new JwtTokenFilter(jwtTokenProvider), BasicAuthenticationFilter.class);
        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getLocalizedMessage()
                            );
                        }
                )
                .and();


        // Entry points
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/**/login").permitAll()

                // Disallow everything else
                .anyRequest().authenticated();


        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));


    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

}





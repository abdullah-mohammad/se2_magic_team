package de.haw.eborrow.configuration;


import de.haw.eborrow.security.jwt.AuthTokenFilter;
import de.haw.eborrow.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

import static org.springframework.web.cors.CorsConfiguration.ALL;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private MyUserDetailsService userDetailsService;

    @Value("${SIGN_UP_URL}")
    private String SIGN_UP_URL;

    @Value("${SIGN_IN_URL}")
    private String SIGN_IN_URL;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    public SecurityConfiguration(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                cors().and().csrf().disable()
                .authorizeRequests()
//                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, SIGN_IN_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/items/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/items/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/get-img/**").permitAll()
                .antMatchers(HttpMethod.GET, "/borrow/**").permitAll()
                // .antMatchers(HttpMethod.POST, "/items/**").permitAll()
                .antMatchers(HttpMethod.GET, "/addresses/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/user-address/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.unmodifiableList(
                Collections.singletonList(ALL)));
        cors.setAllowedMethods(Collections.unmodifiableList(
                Collections.singletonList(ALL)));
        cors.setAllowedHeaders(Collections.unmodifiableList(
                Collections.singletonList(ALL)));
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
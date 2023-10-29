package com.eightbit;

import com.eightbit.util.token.JwtAuthenticationFilter;
import com.eightbit.util.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/Users/**","/Phone/*","/Email/*","/Board/**", "/Articles/**","/Likes/**","/Reports/**","/Comments/**","/ReComments/**","/Files/**").permitAll()
                .antMatchers(HttpMethod.POST, "/Users/**").permitAll()
                .antMatchers(HttpMethod.POST, "/Articles/**","/Likes/**","/Reports/**","/Comments/**","/ReComments/**","/Files/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/Users/**","/Articles/**","/Likes/**","/Reports/**","/Comments/**","/ReComments/**","/Files/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Users/**","/Articles/**","/Likes/**","/Reports/**","/Comments/**","/ReComments/**","/Files/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/Users/**","/Articles/**","/Likes/**","/Reports/**","/Comments/**","/ReComments/**","/Files").authenticated().and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
     }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}

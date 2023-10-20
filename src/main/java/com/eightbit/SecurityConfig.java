package com.eightbit;

import com.eightbit.biz.user.util.JwtAuthenticationFilter;
import com.eightbit.biz.user.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/Users/**","/Board/**").permitAll()
                .antMatchers(HttpMethod.POST, "/Users/check/**","/Users/authkey/**").permitAll()
                .antMatchers(HttpMethod.POST, "/Users/user").permitAll()
                .antMatchers(HttpMethod.POST, "/Board/article/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/Users/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/Board/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Users/token").permitAll()
                .antMatchers(HttpMethod.PATCH, "/Users/token/reset").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Users/point/up").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Board/article/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Board/report/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "Users/**","Board/**").authenticated().and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
     }
}

package com.eightbit;

import com.eightbit.util.token.JwtAuthenticationFilter;
import com.eightbit.util.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{


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
                .antMatchers(HttpMethod.OPTIONS, "/Users/**","/Phone/*","/Email/*","/Board/**", "/Articles/**","/Likes/**","/Reports/**","/Comments/**","/Files/**").permitAll()
                .antMatchers(HttpMethod.POST, "/Users/**").permitAll()
                .antMatchers(HttpMethod.POST, "/Articles/**","/Likes/**","/Reports/**","/Comments/**","/Files/**").authenticated()
                .antMatchers(HttpMethod.POST, "/Articles/aritcle/notice/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/Games/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.POST, "/Shops/**").hasRole("SELLER")
                .antMatchers(HttpMethod.POST, "/Shops/shop/coupone/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.POST, "/Files/files/indie/1/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.POST, "/Files/files/official/1/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.PUT, "/Users/**","/Articles/**","/Likes/**","/Reports/**","/Comments/**","/Files/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Users/**","/Articles/**","/Likes/**","/Reports/**","/Comments/**","/Files/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/Articles/aritcle/notice/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/Games/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.PATCH, "/Shops/**").hasRole("SELLER")
                .antMatchers(HttpMethod.PATCH, "/Shops/shop/coupone/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.DELETE, "/Users/**","/Articles/**","/Likes/**","/Reports/**","/Comments/**","/Files").authenticated()
                .antMatchers(HttpMethod.DELETE, "/Articles/aritcle/notice/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/Games/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.DELETE, "/Shops/**").hasRole("SELLLER")
                .antMatchers(HttpMethod.PATCH, "/Shops/shop/coupone/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.DELETE, "/Files/files/indie/1/**").hasRole("DEVELOPER")
                .antMatchers(HttpMethod.DELETE, "/Files/files/official/1/**").hasRole("DEVELOPER").and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
     }

    @Bean public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
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

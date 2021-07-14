package org.thesix.member.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    public LoginFilter loginFilter() throws Exception{
//        LoginFilter loginFilter = new LoginFilter("/login", authenticationManager());
////        loginFilter.setAuthenticationFailureHandler();
//        return loginFilter;
//    }
}

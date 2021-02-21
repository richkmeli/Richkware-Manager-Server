package it.richkmeli.rms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Spring Security to require only HTTPS requests, commenting this code it enables both protocols.
        //  httpSecurity.requiresChannel()
        // .anyRequest()
        // .requiresSecure();
        // .requiresInsecure();

        // bypass spring security, keeping jframework one
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/**", "/Richkware-Manager-Server/**")
                .anonymous();
/*
        // accessible without authentication,
        httpSecurity.authorizeRequests()
                .antMatchers("/anonymous*")
                .anonymous();

        // login and signup pages
        httpSecurity.authorizeRequests()
                .antMatchers("/login*")
                // .and()
                // .formLogin()
                // .loginPage("/login")
                // .and()
                // .logout()
                .permitAll();

        // accessible after a successful login
        httpSecurity.authorizeRequests()
                .anyRequest()
                .authenticated();
    */
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("richk@i.it").password(new BCryptPasswordEncoder().encode("00000000")).roles("USER")
//                .and()
//                .withUser("admin").password("admin").roles("ADMIN");
//    }
//
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


}

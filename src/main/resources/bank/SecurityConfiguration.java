package bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 1:19 am
 * 4
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;


    /**
     *
     * create a password encode bean
     *
     * @return org.springframework.security.crypto.password.PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     *
     * Configuration relating to the authentication:
     *      1. config custom UserDetailsService
     *      2. config a passwordEncoder
     *
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //config custom UserDetailsService
       auth.userDetailsService(userDetailsService)
               //config a passwordEncoder
               .passwordEncoder(passwordEncoder());
    }

//    /**
//     *
//     * Config the path /register should connect without authentication
//     *
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/api/user/register");
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginProcessingUrl("/api/user/login").permitAll().and().
//                addFilterBefore(new RESTAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class).
                authorizeRequests().
                antMatchers("/api/user/login").permitAll().
                antMatchers("/api/user/register*").permitAll().
                anyRequest().authenticated();
//                and().exceptionHandling().authenticationEntryPoint(new RESTUnauthenticatedRequestHandler()).
//                and().httpBasic().authenticationEntryPoint(new RESTUnauthenticatedRequestHandler());

    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}

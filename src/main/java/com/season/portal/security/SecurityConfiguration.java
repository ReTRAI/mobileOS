package com.season.portal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        /**/
        http.authorizeRequests()
                .antMatchers("/resselers","/resseler/**").hasRole("ADMIN")
                .antMatchers("/dashboard", "/getTranslation").hasAnyRole("SUPPORT","RESSELER", "ADMIN")
                .antMatchers( "/getIndexTranslation", "/getIndexTranslation/", "/getIndexTranslation/**").permitAll()
                .antMatchers( "/errorHandler").permitAll()
                /*
                .and().formLogin()
                    .loginPage("/login")
                    .failureHandler((req,res,exp)->{  // Failure handler invoked after authentication failure

                        res.sendRedirect("/loginError"); // Redirect user to login page with error message.
                    })
                    .defaultSuccessUrl("/dashboard")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .permitAll()
                */
                .and().logout().logoutSuccessUrl("/login")
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        /**/
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}

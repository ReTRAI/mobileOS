package com.season.portal.security;

import com.season.portal.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import javax.annotation.Resource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){return new CustomAccessDeniedHandler();}
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    /*
    @Resource
    CustomAuthProvider customAuthProvider;
    */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService);
        //auth.authenticationProvider(customAuthProvider);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**/
        http
            .requiresChannel().anyRequest().requiresSecure()

            .and()
                .authorizeRequests()
                    .antMatchers("/resselers","/resseler/**").hasAnyRole("ADMIN")
                    .antMatchers("/support","/support/**").hasAnyRole("SUPPORT", "ADMIN")
                    .antMatchers("/dashboard").hasAnyRole("SUPPORT","RESSELER", "ADMIN")

                    .antMatchers( "/", "/login", "/logout").permitAll()
                    .antMatchers( "/getIndexTranslation").permitAll()
                    .antMatchers( "/errorHandler", "/error").permitAll()
                    .antMatchers( "/css/**", "/ico/**", "/js/**", "/media/**", "/vendors/**").permitAll()
                    .anyRequest().authenticated()
            .and()
                //.x509()
                //.x509AuthenticationFilter(getCustomFilter())

                //.and()
                .logout()
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)

                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/login")
                    .addLogoutHandler(new HeaderWriterLogoutHandler(
                        new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)))
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
        ;
        /**/
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

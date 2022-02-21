package com.season.portal.configuration;

import com.season.portal.auth.CustomAuthenticationProvider;
import com.season.portal.auth.MyUserDetailsService;
import com.season.portal.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
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
    /*@Autowired
    UserDetailsService userDetailsService;*/

    @Autowired
    private CustomAuthenticationProvider customAuthProvider;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){return new CustomAccessDeniedHandler();}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(customAuthProvider);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**/
        http
            .requiresChannel().anyRequest().requiresSecure()

            .and()
                .authorizeRequests()
                    .antMatchers("/toggleAdminView").hasRole("ADMIN")
                    .antMatchers("/resellers","/reseller/**").hasAnyRole("ADMIN", "RESELLER")
                    .antMatchers("/devices","/device").hasAnyRole("ADMIN", "RESELLER")

                    .antMatchers("/support","/support/**").hasAnyRole("ADMIN", "SUPPORT")
                    .antMatchers("/users","/users/**").hasAnyRole("ADMIN", "RESELLER")

                    .antMatchers("/dashboard").hasAnyRole("SUPPORT","RESELLER", "ADMIN")

                    .antMatchers( "/", "/login", "/logout").permitAll()
                    .antMatchers( "/getIndexTranslation").permitAll()
                    .antMatchers( "/errorHandler", "/error").permitAll()
                    .antMatchers( "/css/**", "/ico/**", "/js/**", "/media/**", "/vendors/**").permitAll()
                    .anyRequest().authenticated()
            .and()
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

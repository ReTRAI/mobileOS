package com.season.portal.configuration;

import com.season.portal.auth.CustomAuthenticationProvider;
import com.season.portal.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class AnnotationSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    public static final String ALLOW_ROLES_ALL = "hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPPORT') or hasRole('ROLE_RESELLER')";
    public static final String ALLOW_ROLES_SUP_ADMIN = "hasRole('ROLE_SUPPORT') or hasRole('ROLE_ADMIN')";
    public static final String ALLOW_ROLES_RES_ADMIN = "hasRole('ROLE_RESELLER') or hasRole('ROLE_ADMIN')";
    public static final String ALLOW_ROLES_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String ALLOW_ROLES_SUP = "hasRole('ROLE_SUPPORT')";
}

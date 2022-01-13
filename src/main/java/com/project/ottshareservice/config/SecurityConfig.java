package com.project.ottshareservice.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableWebSecurity
@EnableAsync
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_CHECKED_USER > ROLE_USER");
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/login", "/logout").permitAll()
                .antMatchers("/share/*").permitAll()
                .antMatchers("/share/*/**").authenticated()
                .mvcMatchers("/settings/**", "/check-email").authenticated()
                .mvcMatchers("/new-share").hasRole("CHECKED_USER")
                .mvcMatchers("/sign-up").anonymous()
                .expressionHandler(expressionHandler());

        http.formLogin()
                .loginPage("/login").permitAll();

        http.logout()
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}

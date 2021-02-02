package com.orgella.usersmanagement.infrastructure.configuration

import com.orgella.usersmanagement.domain.service.UserService
import com.orgella.usersmanagement.infrastructure.configuration.security.AuthenticationFilter
import com.orgella.usersmanagement.infrastructure.configuration.security.AuthorizationFilter
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurity(
    private val environment: Environment,
    private val usersService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/users")
            .hasIpAddress(environment.getProperty("gateway.ip"))
            .anyRequest().authenticated()
            .and()
            .addFilter(getAuthenticationFilter())
            .addFilter(
                AuthorizationFilter(
                    authenticationManager(),
                    environment,
                    usersService
                )
            )
            .logout()
            .invalidateHttpSession(true)
            .deleteCookies("UserInfo")
            .logoutSuccessHandler((HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
        http.headers().frameOptions().disable()
    }

    private fun getAuthenticationFilter(): AuthenticationFilter {
        val authenticationFilter = AuthenticationFilter(usersService, environment, authenticationManager())

        authenticationFilter.setFilterProcessesUrl(
            environment.getProperty("login.url.path")
        )
        return authenticationFilter
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(usersService)
            .passwordEncoder(bCryptPasswordEncoder)
    }
}
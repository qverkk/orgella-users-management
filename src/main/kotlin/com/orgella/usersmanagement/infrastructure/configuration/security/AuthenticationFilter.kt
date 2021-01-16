package com.orgella.usersmanagement.infrastructure.configuration.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.orgella.usersmanagement.application.request.LoginRequest
import com.orgella.usersmanagement.domain.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    private val usersService: UserService,
    private val env: Environment,
    private val authManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {
    init {
        super.setAuthenticationManager(authManager)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val creds = ObjectMapper().readValue(
            request.inputStream,
            LoginRequest::class.java
        )

        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                creds.username,
                creds.password,
                emptyList()
            )
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val username = (authResult.principal as User).username

        val user = usersService.findUserByUsername(username)

        user.ifPresent {
            val token = Jwts.builder()
                .setSubject(it.id.toString())
                .claim("roles", it.roles.map { role -> role.name }.joinToString(separator = ",", prefix = "[", postfix = "]"))
                .setExpiration(
                    Date(
                        System.currentTimeMillis() + env.getProperty("token.expiration_time")!!.toLong()
                    )
                )
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact()

            response.addHeader("token", token)
            response.addHeader("userId", it.id.toString())
            response.addHeader("roles", it.roles.map { role -> role.name }.joinToString(separator = ",", prefix = "[", postfix = "]"))
        }
    }
}

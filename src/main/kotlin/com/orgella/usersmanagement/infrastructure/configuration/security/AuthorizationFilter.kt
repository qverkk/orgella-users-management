package com.orgella.usersmanagement.infrastructure.configuration.security

import com.orgella.usersmanagement.domain.service.UserDetailsImpl
import com.orgella.usersmanagement.domain.service.UserService
import io.jsonwebtoken.Jwts
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(
    authManager: AuthenticationManager,
    private val env: Environment,
    private val userService: UserService
) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
//        val authorizationHeader: String? = request.getHeader(env.getProperty("authorization.token.header.name"))
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix")!!)) {
//            chain.doFilter(request, response)
//            return
//        }

        val cookies: Array<out Cookie>? = request.cookies
        val cookie: Cookie? = cookies?.first { it.name == "UserInfo" }

        if (cookie == null) {
            chain.doFilter(request, response)
            return
        }

        val authentication = getAuthentication(request)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {

//        val authorizationHeader = request.getHeader(env.getProperty("authorization.token.header.name")) ?: return null
//
//        val token = authorizationHeader.replace(
//            env.getProperty("authorization.token.header.prefix")!!,
//            ""
//        )
//
//        val userId = Jwts.parser()
//            .setSigningKey(env.getProperty("token.secret"))
//            .parseClaimsJws(token)
//            .body
//            .subject ?: return null
        val cookie = request.cookies.first { it.name == "UserInfo" }
        val userId = Jwts.parser()
            .setSigningKey(env.getProperty("token.secret"))
            .parseClaimsJws(cookie.value)
            .body
            .subject ?: return null

        val user = userService.findUserByUUID(UUID.fromString(userId)).orElseGet { null } ?: return null

        val userDetails = UserDetailsImpl.build(user)

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
}
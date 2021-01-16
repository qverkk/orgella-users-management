package com.orgella.usersmanagement.infrastructure.configuration

import com.orgella.usersmanagement.domain.repository.RoleRepository
import com.orgella.usersmanagement.domain.repository.UserRepository
import com.orgella.usersmanagement.domain.service.DomainRoleService
import com.orgella.usersmanagement.domain.service.DomainUserService
import com.orgella.usersmanagement.domain.service.RoleService
import com.orgella.usersmanagement.domain.service.UserService
import feign.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BeanConfiguration {

    @Bean
    fun userService(userRepository: UserRepository): UserService {
        return DomainUserService(userRepository, bCryptPasswordEncoder())
    }

    @Bean
    fun roleService(roleRepository: RoleRepository): RoleService {
        return DomainRoleService(roleRepository)
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder(11)

    @Bean
    @Profile("production")
    fun feignProductionLoggingLevel(): Logger.Level = Logger.Level.NONE

    @Bean
    @Profile("!production")
    fun feignDefaultFeignLoggingLevel(): Logger.Level = Logger.Level.FULL
}
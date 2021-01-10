package com.orgella.usersmanagement.domain.service

import com.orgella.usersmanagement.domain.UserEntity
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService : UserDetailsService {

    fun addUser(userEntity: UserEntity)

    fun enableUser(id: UUID)

    fun findUserByUsername(username: String): Optional<UserEntity>

    fun lockUser(id: UUID)

    fun unlockUser(id: UUID)
}
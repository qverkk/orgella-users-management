package com.orgella.usersmanagement.domain.service

import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.UserEntity
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService : UserDetailsService {

    fun addUser(userEntity: UserEntity)

    fun enableUser(id: UUID)

    fun findUserByUsername(username: String): Optional<UserEntity>

    fun findUserByUUID(uuid: UUID): Optional<UserEntity>

    fun addRoleForUsername(role: RoleEntity, username: String)

    fun lockUser(id: UUID)

    fun unlockUser(id: UUID)

    fun findAll(): List<UserEntity>

    fun removeRoleForUsername(role: RoleEntity, username: String)
}
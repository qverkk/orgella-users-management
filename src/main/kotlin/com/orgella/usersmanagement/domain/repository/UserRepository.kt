package com.orgella.usersmanagement.domain.repository

import com.orgella.usersmanagement.domain.UserEntity
import java.util.*

interface UserRepository {

    fun findById(id: UUID): Optional<UserEntity>

    fun findByUsernameOrEmail(username: String, email: String): Optional<UserEntity>

    fun save(userEntity: UserEntity)

    fun findByUsername(username: String): Optional<UserEntity>

    fun findAll(): List<UserEntity>
}
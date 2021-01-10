package com.orgella.usersmanagement.infrastructure.repository.mongo

import com.orgella.usersmanagement.domain.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface SpringDataMongoUsersRepository: MongoRepository<UserEntity, UUID> {

    fun findByUsernameOrEmail(username: String, email: String): Optional<UserEntity>

    fun findByUsername(username: String): Optional<UserEntity>
}

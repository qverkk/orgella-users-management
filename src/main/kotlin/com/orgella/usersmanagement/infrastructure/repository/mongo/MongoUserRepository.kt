package com.orgella.usersmanagement.infrastructure.repository.mongo

import com.orgella.usersmanagement.domain.UserEntity
import com.orgella.usersmanagement.domain.repository.UserRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.*

@Component
@Primary
class MongoUserRepository(
    val usersRepository: SpringDataMongoUsersRepository
) : UserRepository {

    override fun findById(id: UUID): Optional<UserEntity> {
        return usersRepository.findById(id)
    }

    override fun findByUsernameOrEmail(username: String, email: String): Optional<UserEntity> {
        return usersRepository.findByUsernameOrEmail(username, email)
    }

    override fun save(userEntity: UserEntity) {
        usersRepository.save(userEntity)
    }

    override fun findByUsername(username: String): Optional<UserEntity> {
        return usersRepository.findByUsername(username)
    }

    override fun findAll(): List<UserEntity> {
        return usersRepository.findAll()
    }
}
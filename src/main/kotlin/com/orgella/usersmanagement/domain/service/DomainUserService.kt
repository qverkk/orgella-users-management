package com.orgella.usersmanagement.domain.service

import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.UserEntity
import com.orgella.usersmanagement.domain.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class DomainUserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun addUser(userEntity: UserEntity) {
        if (userRepository.findByUsernameOrEmail(userEntity.username, userEntity.email).isPresent) {
            throw UserAlreadyExistsException(userEntity.username, userEntity.email)
        }

        userEntity.password = passwordEncoder.encode(userEntity.password)
        userRepository.save(userEntity)
    }

    override fun enableUser(id: UUID) {
        userRepository.findById(id).ifPresentOrElse(
            {
                it.enabled = true
                userRepository.save(it)
            }, {
                throw UserIdDoesntExistException(id)
            })
    }

    override fun findUserByUsername(username: String): Optional<UserEntity> {
        return userRepository.findByUsername(username)
    }

    override fun findUserByUUID(uuid: UUID): Optional<UserEntity> {
        return userRepository.findById(uuid)
    }

    override fun addRoleForUsername(role: RoleEntity, username: String) {
        userRepository.findByUsername(username).ifPresent {
            it.roles.add(role)
            userRepository.save(it)
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseGet { throw UsernameNotFoundException(username) }

        return User(
            user.username,
            user.password,
            emptyList()
        )
    }

    override fun lockUser(id: UUID) {
        userRepository.findById(id).ifPresentOrElse(
            {
                it.locked = true
                userRepository.save(it)
            }, {
                throw UserIdDoesntExistException(id)
            })
    }

    override fun unlockUser(id: UUID) {
        userRepository.findById(id).ifPresentOrElse(
            {
                it.locked = false
                userRepository.save(it)
            }, {
                throw UserIdDoesntExistException(id)
            })
    }
}
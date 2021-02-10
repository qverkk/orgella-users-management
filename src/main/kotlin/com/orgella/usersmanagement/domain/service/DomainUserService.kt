package com.orgella.usersmanagement.domain.service

import com.orgella.usersmanagement.domain.ERole
import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.UserEntity
import com.orgella.usersmanagement.domain.repository.RoleRepository
import com.orgella.usersmanagement.domain.repository.UserRepository
import org.springframework.core.env.Environment
import org.springframework.data.domain.Page
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import javax.annotation.PostConstruct

class DomainUserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val environment: Environment
) : UserService {

    @PostConstruct
    fun initAdminAccount() {
        val adminUser = userRepository.findByUsername(environment.getProperty("admin.username")!!).orElse(null)
        if (adminUser != null
            || environment.getProperty("admin.username") == null
            || environment.getProperty("admin.password") == null
        ) {
            return
        }

        var adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.name).orElse(null)
        if (adminRole == null) {
            adminRole = roleRepository.save(
                RoleEntity(
                    UUID.randomUUID(),
                    ERole.ROLE_ADMIN,
                    0
                )
            )
        }

        userRepository.save(
            UserEntity(
                UUID.randomUUID(),
                "Admin",
                "Admin",
                Date(),
                environment.getProperty("admin.username")!!,
                "admin@orgella.com",
                passwordEncoder.encode(environment.getProperty("admin.password")!!),
                enabled = true,
                locked = false,
                roles = mutableSetOf(adminRole),
                version = 0
            )
        )
    }

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

    override fun findAll(page: Int): Page<UserEntity> {
        return userRepository.findAll(page)
    }

    override fun removeRoleForUsername(role: RoleEntity, username: String) {
        userRepository.findByUsername(username).ifPresent {
            it.roles.remove(role)
            userRepository.save(it)
        }
    }
}
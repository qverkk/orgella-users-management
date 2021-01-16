package com.orgella.usersmanagement.infrastructure.repository.mongo

import com.orgella.usersmanagement.domain.ERole
import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.repository.RoleRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.*

@Component
@Primary
class MongoRoleRepository(
    val roleRepository: SpringDateMongoRoleRepository
) : RoleRepository {
    override fun save(role: RoleEntity): RoleEntity {
        return roleRepository.save(role)
    }

    override fun findByName(name: String): Optional<RoleEntity> {
        return roleRepository.findByName(ERole.valueOf(name))
    }

    override fun deleteByName(name: String) {
        roleRepository.findByName(ERole.valueOf(name)).ifPresent {
            roleRepository.delete(it)
        }
    }
}
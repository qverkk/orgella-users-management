package com.orgella.usersmanagement.domain.service

import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.repository.RoleRepository
import java.util.*

class DomainRoleService(
    private val roleRepository: RoleRepository
) : RoleService {
    override fun addRole(role: RoleEntity): RoleEntity {
        return roleRepository.save(role)
    }

    override fun findRoleByName(name: String): Optional<RoleEntity> {
        return roleRepository.findByName(name)
    }

    override fun deleteRoleByName(name: String) {
        roleRepository.deleteByName(name)
    }
}
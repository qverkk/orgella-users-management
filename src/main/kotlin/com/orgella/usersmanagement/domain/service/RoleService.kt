package com.orgella.usersmanagement.domain.service

import com.orgella.usersmanagement.domain.RoleEntity
import java.util.*

interface RoleService {

    fun addRole(role: RoleEntity): RoleEntity

    fun findRoleByName(name: String): Optional<RoleEntity>

    fun deleteRoleByName(name: String)
}

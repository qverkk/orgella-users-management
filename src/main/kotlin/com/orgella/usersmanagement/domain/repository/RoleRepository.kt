package com.orgella.usersmanagement.domain.repository

import com.orgella.usersmanagement.domain.RoleEntity
import java.util.*

interface RoleRepository {

    fun save(role: RoleEntity): RoleEntity

    fun findByName(name: String): Optional<RoleEntity>

    fun deleteByName(name: String)
}

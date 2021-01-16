package com.orgella.usersmanagement.infrastructure.repository.mongo

import com.orgella.usersmanagement.domain.ERole
import com.orgella.usersmanagement.domain.RoleEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface SpringDateMongoRoleRepository : MongoRepository<RoleEntity, Long> {

    fun findByName(name: ERole): Optional<RoleEntity>
}

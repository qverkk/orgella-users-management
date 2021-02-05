package com.orgella.usersmanagement.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import java.util.*

data class RoleEntity(
    @field:Id
    var id: UUID,
    var name: ERole,
    @field:Version
    val version: Int
)

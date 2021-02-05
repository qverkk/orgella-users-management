package com.orgella.usersmanagement.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import java.util.*

data class UserEntity(
    @field:Id
    var id: UUID,
    var firstName: String,
    var lastName: String,
    var dateOfBirth: Date,
    var username: String,
    var email: String,
    var password: String,
    var enabled: Boolean,
    var locked: Boolean,
    var roles: MutableSet<RoleEntity>,
    @field:Version
    val version: Int
)
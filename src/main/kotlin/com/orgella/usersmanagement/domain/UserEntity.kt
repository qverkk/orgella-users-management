package com.orgella.usersmanagement.domain

import java.util.*

data class UserEntity(
    var id: UUID,
    var firstName: String,
    var lastName: String,
    var dateOfBirth: Date,
    var username: String,
    var email: String,
    var password: String,
    var enabled: Boolean,
    var locked: Boolean,
    var roles: MutableSet<RoleEntity>
)
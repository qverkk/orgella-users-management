package com.orgella.usersmanagement.application.response

import java.util.*

data class UserDetailsResponse(
    val userId: UUID,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Date,
    val roles: List<String>
)

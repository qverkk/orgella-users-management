package com.orgella.usersmanagement.application.mappers

import com.orgella.usersmanagement.application.request.CreateUserRequest
import com.orgella.usersmanagement.domain.UserEntity
import java.text.SimpleDateFormat
import java.util.*

fun CreateUserRequest.toDomain(): UserEntity {
    return UserEntity(
        UUID.randomUUID(),
        this.firstName,
        this.lastName,
        SimpleDateFormat("dd-MM-yyyy").parse(this.dateOfBirth),
        this.username,
        this.email,
        this.password,
        enabled = true,
        locked = false
    )
}
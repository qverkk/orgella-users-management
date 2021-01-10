package com.orgella.usersmanagement.domain.service

import java.util.*

class UserIdDoesntExistException(id: UUID) : RuntimeException("User id $id doesn't exist")

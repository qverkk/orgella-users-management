package com.orgella.usersmanagement.exceptions.handlers

data class ConstraintViolationError(
    val cause: String,
    val field: String
)

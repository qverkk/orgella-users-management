package com.orgella.usersmanagement.exceptions.handlers

class ConstraintViolationErrorResponse(
    message: String,
    error: String,
    val violations: List<ConstraintViolationError>
) : EndpointError(message, error)

package com.orgella.usersmanagement.domain.service

class UserAlreadyExistsException(username: String, email: String) :
    RuntimeException("Email $email or username $username is already taken")

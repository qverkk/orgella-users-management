package com.orgella.usersmanagement.application.request

class LoginRequest(
    var username: String,
    var password: String
) {

    constructor(): this("", "")
}
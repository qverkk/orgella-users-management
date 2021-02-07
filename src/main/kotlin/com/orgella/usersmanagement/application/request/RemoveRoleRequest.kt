package com.orgella.usersmanagement.application.request

data class RemoveRoleRequest(
    var username: String,
    var roleName: String
)

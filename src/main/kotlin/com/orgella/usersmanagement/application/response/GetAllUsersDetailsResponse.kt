package com.orgella.usersmanagement.application.response

data class GetAllUsersDetailsResponse(
    val page: Int,
    val maxPages: Int,
    val users: List<UserDetailsResponse>
)

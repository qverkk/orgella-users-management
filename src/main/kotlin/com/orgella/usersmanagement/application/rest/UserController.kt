package com.orgella.usersmanagement.application.rest

import com.orgella.usersmanagement.application.mappers.toDomain
import com.orgella.usersmanagement.application.request.CreateUserRequest
import com.orgella.usersmanagement.application.response.CreateUserResponse
import com.orgella.usersmanagement.domain.ERole
import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.service.RoleService
import com.orgella.usersmanagement.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("users")
class UserController(
    private val userService: UserService,
    private val roleService: RoleService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createUser(@Valid @RequestBody createUserRequest: CreateUserRequest): ResponseEntity<CreateUserResponse> {

        var role = roleService.findRoleByName("ROLE_USER").orElseGet { null }
        if (role == null) {
            role = roleService.addRole(
                RoleEntity(
                    UUID.randomUUID(), ERole.ROLE_USER
                )
            )
        }

        val userDomain = createUserRequest.toDomain()

        userService.addUser(userDomain)
        userService.addRoleForUsername(role, userDomain.username)

        val returnValue = CreateUserResponse(userDomain.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue)
    }

    @GetMapping(value = ["/{username}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserByUserId(@PathVariable username: String): ResponseEntity<CreateUserResponse>? {
        val user = userService.findUserByUsername(username).orElseGet { null } ?: return null

        return ResponseEntity.ok(CreateUserResponse(user.id))
    }

}
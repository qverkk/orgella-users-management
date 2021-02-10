package com.orgella.usersmanagement.application.rest

import com.orgella.usersmanagement.application.mappers.toDomain
import com.orgella.usersmanagement.application.request.AddRoleRequest
import com.orgella.usersmanagement.application.request.CreateUserRequest
import com.orgella.usersmanagement.application.request.RemoveRoleRequest
import com.orgella.usersmanagement.application.response.CreateUserResponse
import com.orgella.usersmanagement.application.response.GetAllRolesResponse
import com.orgella.usersmanagement.application.response.GetAllUsersDetailsResponse
import com.orgella.usersmanagement.application.response.UserDetailsResponse
import com.orgella.usersmanagement.domain.ERole
import com.orgella.usersmanagement.domain.RoleEntity
import com.orgella.usersmanagement.domain.service.RoleService
import com.orgella.usersmanagement.domain.service.UserDetailsImpl
import com.orgella.usersmanagement.domain.service.UserService
import com.orgella.usersmanagement.exceptions.CannotProcessCurrentUserException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
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
                    UUID.randomUUID(), ERole.ROLE_USER, 0
                )
            )
        }

        val userDomain = createUserRequest.toDomain()

        userService.addUser(userDomain)
        userService.addRoleForUsername(role, userDomain.username)

        val returnValue = CreateUserResponse(userDomain.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = ["/addRole"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRole(@RequestBody addRoleRequest: AddRoleRequest) {
        var role = roleService.findRoleByName(addRoleRequest.roleName).orElseGet { null }
        if (role == null) {
            role = roleService.addRole(
                RoleEntity(
                    UUID.randomUUID(), ERole.valueOf(addRoleRequest.roleName), 0
                )
            )
        }
        userService.addRoleForUsername(role, addRoleRequest.username)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = ["/removeRole"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun removeRole(@RequestBody removeRoleRequest: RemoveRoleRequest) {
        var role = roleService.findRoleByName(removeRoleRequest.roleName).orElseGet { null }
        if (role == null) {
            role = roleService.addRole(
                RoleEntity(
                    UUID.randomUUID(), ERole.valueOf(removeRoleRequest.roleName), 0
                )
            )
        }

        userService.removeRoleForUsername(role, removeRoleRequest.username)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roles/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllAvailableRoles(): ResponseEntity<GetAllRolesResponse> {
        return ResponseEntity.ok(GetAllRolesResponse(ERole.values().map { it.name }))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllUsersDetails(
        @RequestParam(defaultValue = "0") page: Int
    ): ResponseEntity<GetAllUsersDetailsResponse> {
        val users = userService.findAll(page)

        return ResponseEntity.ok(
            GetAllUsersDetailsResponse(
                users.number,
                users.totalPages,
                users.content.map {
                    UserDetailsResponse(
                        it.id,
                        it.username,
                        it.email,
                        it.firstName,
                        it.lastName,
                        it.dateOfBirth,
                        it.roles.map { role -> role.name.name }
                    )
                }
            )
        )
    }

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(value = ["/{username}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserDetailsByUsername(@PathVariable username: String): ResponseEntity<UserDetailsResponse>? {
        val user = userService.findUserByUsername(username).orElseGet { null } ?: return null

        return ResponseEntity.ok(
            UserDetailsResponse(
                user.id,
                user.username,
                user.email,
                user.firstName,
                user.lastName,
                user.dateOfBirth,
                user.roles.map { it.name.name }
            )
        )
    }

    @GetMapping("/me", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCurrentUserDetails(authentication: Authentication): ResponseEntity<UserDetailsResponse> {
        val user = authentication.principal as UserDetailsImpl
        val userEntity = userService.findUserByUsername(user.username).orElseThrow {
            throw CannotProcessCurrentUserException("Sorry, we can't process your request at the moment.")
        }

        return ResponseEntity.ok(
            UserDetailsResponse(
                userEntity.id,
                userEntity.username,
                userEntity.email,
                userEntity.firstName,
                userEntity.lastName,
                userEntity.dateOfBirth,
                userEntity.roles.map { it.name.name }
            )
        )
    }
}
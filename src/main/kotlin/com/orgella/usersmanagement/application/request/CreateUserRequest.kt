package com.orgella.usersmanagement.application.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class CreateUserRequest(
    @field:NotNull(message = "First name cannot be empty")
    @field:Size(message = "First name must have more than 2 characters", min = 2)
    var firstName: String,
    @field:NotNull(message = "Last name cannot be empty")
    @field:Size(message = "Last name must have more than 2 characters", min = 2)
    var lastName: String,
    @field:NotNull(message = "Date of birth cannot be empty")
    @field:Pattern(
        regexp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$",
        message = "Date must be in format DD-mm-YYYY"
    )
    var dateOfBirth: String,
    @field:Size(message = "Username must have more than 3 characters", min = 3)
    var username: String,
    @field:Email(message = "Email must be valid")
    var email: String,
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$",
        message = "Password must contain minimum 8 characters, 1 number, 1 special character, 1 upper case letter, 1 lower case letter"
    )
    var password: String
)
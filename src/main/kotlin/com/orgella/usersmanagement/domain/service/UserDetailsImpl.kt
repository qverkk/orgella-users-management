package com.orgella.usersmanagement.domain.service

import com.fasterxml.jackson.annotation.JsonIgnore
import com.orgella.usersmanagement.domain.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

class UserDetailsImpl(
    var userId: UUID,
    var auth: Collection<GrantedAuthority>,
    @field:JsonIgnore var pass: String,
    var usrname: String,
    var active: Boolean,
    var lock: Boolean
) : UserDetails {

    companion object {
        fun build(user: UserEntity): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                .map { role -> SimpleGrantedAuthority(role.name.name) }
                .collect(Collectors.toList())

            return UserDetailsImpl(user.id, authorities, user.password, user.username, user.enabled, user.locked)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        auth as MutableCollection<out GrantedAuthority>

    override fun getPassword(): String = pass

    override fun getUsername(): String = usrname

    override fun isAccountNonExpired(): Boolean = false

    override fun isAccountNonLocked(): Boolean = !lock

    override fun isCredentialsNonExpired(): Boolean = false

    override fun isEnabled(): Boolean = active

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserDetailsImpl
        return userId == user.userId
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + auth.hashCode()
        result = 31 * result + pass.hashCode()
        result = 31 * result + usrname.hashCode()
        result = 31 * result + active.hashCode()
        result = 31 * result + lock.hashCode()
        return result
    }
}

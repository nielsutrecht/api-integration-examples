package security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface UserRepository : UserDetailsService {
    fun loadUserByToken(token: String): UserDetails
    fun addUser(username: String, password: String, vararg roles: String) : Pair<UserDetails, String>
}

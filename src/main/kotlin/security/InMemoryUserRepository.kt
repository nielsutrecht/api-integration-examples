package security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct

@Repository
class InMemoryUserRepository(
        private val encoder: BCryptPasswordEncoder
) : UserRepository {
    private val users = mutableMapOf<String, UserDetails>()
    private val tokenMap = mutableMapOf<String, UserDetails>()

    override fun loadUserByUsername(name: String): UserDetails = users[name]
            ?: throw IllegalArgumentException("User $name not found")

    override fun loadUserByToken(token: String): UserDetails = tokenMap[token]
            ?: throw IllegalArgumentException("User token expired")

    override fun addUser(username: String, password: String, vararg roles: String): Pair<UserDetails, String> {
        val user = User.withUsername(username)
                .password(password)
                .roles(*roles)
                .passwordEncoder(encoder::encode)
                .build()

        users[user.username] = user

        return user to ""
    }

    @PostConstruct
    fun init() {
        addUser("john", "secret", "USER")
        addUser("jane", "supersecret", "USER", "ADMIN")
    }
}

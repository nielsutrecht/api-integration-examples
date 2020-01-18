package security

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @PostMapping("/user/login")
    fun login(@RequestBody userLoginRequest: UserLoginRequest) : UserLoginResponse {
        return UserLoginResponse("abcdef")
    }

    @DeleteMapping("/user/login")
    fun logout(): ResponseEntity<Unit> {
        return ResponseEntity.noContent().build()
    }

    data class UserLoginRequest(val username: String, val password: String)
    data class UserLoginResponse(val token: String)
}

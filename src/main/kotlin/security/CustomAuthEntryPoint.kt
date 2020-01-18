package security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.nibado.example.oauth.errorhandling.Problem
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthEntryPoint : BasicAuthenticationEntryPoint() {
    override fun commence(request: HttpServletRequest,
                 response: HttpServletResponse,
                 authException: AuthenticationException) {
        //Authentication failed, send error response.
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.addHeader("WWW-Authenticate", "Basic realm=$realmName")

        response.writer.use { println(MAPPER.writeValueAsString(UNAUTHORIZED_PROBLEM)) }
    }

    override fun afterPropertiesSet() {
        realmName = WebSecurityConfig.REALM
        super.afterPropertiesSet()
    }

    companion object {
        private val UNAUTHORIZED_PROBLEM = Problem("Unauthorized", "Please authorize using basic authentication", HttpStatus.UNAUTHORIZED.value())
        private val MAPPER = ObjectMapper().registerKotlinModule()
    }
}

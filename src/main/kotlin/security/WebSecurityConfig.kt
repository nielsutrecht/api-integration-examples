package security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


class WebSecurityConfig(
        private val userRepository: UserRepository,
        private val tokenFilter: AuthorizationTokenFilter
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()

                .antMatchers("/user/login").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()

                .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun getBasicAuthEntryPoint(): CustomAuthEntryPoint {
        return CustomAuthEntryPoint()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Autowired
    fun configureGlobalSecurity(auth: AuthenticationManagerBuilder, encoder: BCryptPasswordEncoder) {
        auth.userDetailsService(userRepository).passwordEncoder(encoder)
    }

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
    }

    companion object {
        const val REALM = "API_INTEGRATION"
    }
}
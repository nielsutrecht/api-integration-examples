package com.nibado.example.oauth.spotify

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "spotify")
class SpotifyConfig {
    lateinit var clientId : String
    lateinit var clientSecret: String
    lateinit var redirectUrl: String
}
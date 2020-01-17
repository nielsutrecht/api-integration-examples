package com.nibado.example.oauth.spotify

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.net.URLEncoder
import java.time.ZonedDateTime
import java.util.*

@Component
class SpotifyClient(private val restTemplate: RestTemplate, private val spotifyConfig: SpotifyConfig) {
    fun getToken(code: String) : TokenResponse {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.setBasicAuth(spotifyConfig.clientId, spotifyConfig.clientSecret)

        val body = LinkedMultiValueMap<String, String>()
        body["grant_type"] = "authorization_code"
        body["code"] = code
        body["redirect_uri"] = spotifyConfig.redirectUrl

        val entity = HttpEntity(body, headers)

        val response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, TokenResponse::class.java)

        if(response.statusCode.is2xxSuccessful) {
            return response.body!!
        } else {
            throw RuntimeException("Error calling POST $TOKEN_URL")
        }
    }

    fun getRecentlyPlayed(token: String, limit: Int = 50) : RecentlyPlayed {
        val authHeader = "Bearer $token"

        val entity = HttpEntity(null, authHeaders(token))

        val response = restTemplate.exchange(RECENTLY_PLAYED_URL, HttpMethod.GET, entity, RecentlyPlayed::class.java, mapOf("limit" to limit))

        if(response.statusCode.is2xxSuccessful) {
            return response.body!!
        } else {
            throw RuntimeException("Error calling GET $RECENTLY_PLAYED_URL")
        }
    }

    private fun authHeaders(token: String) = HttpHeaders().also { it.setBearerAuth(token) }

    data class TokenResponse(
        @JsonAlias("access_token") val accessToken: String,
        @JsonAlias("refresh_token") val refreshToken: String,
        @JsonAlias("expires_in") val expiresIn: Int
    )

    data class RecentlyPlayed(val items: List<PlayedItem>)
    data class PlayedItem(val track: Track, @JsonAlias("played_at") val playedAt: ZonedDateTime)
    data class Track(val artists: List<Artist>, @JsonAlias("duration_ms") val duration: Int, val name: String)
    data class Artist(val name: String)

    companion object {
        private const val RECENTLY_PLAYED_URL = "https://api.spotify.com/v1/me/player/recently-played"
        private const val TOKEN_URL = "https://accounts.spotify.com/api/token"
    }
}
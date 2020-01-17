package com.nibado.example.oauth.spotify

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.net.URLEncoder

@Controller
@RequestMapping("/spotify")
class SpotifyController(private val spotifyConfig: SpotifyConfig, private val spotifyClient: SpotifyClient) {
    private val scopes = listOf("user-read-private", "user-read-recently-played")
        .joinToString(" ")

    @GetMapping
    fun index() = ModelAndView().also {
        it.viewName = "spotify/index"

        it.addObject("clientId", spotifyConfig.clientId)
        it.addObject("redirectUrl", spotifyConfig.redirectUrl)
        it.addObject("scopes", scopes)
        it.addObject("state", 1234)
    }

    @GetMapping("/secret")
    fun secret(@RequestParam code: String, @RequestParam state: String) = ModelAndView().also {
        it.viewName = "spotify/recently-played"
        val token = spotifyClient.getToken(code).accessToken
        it.addObject("token", token)
        it.addObject("recent", spotifyClient.getRecentlyPlayed(token).items)
    }
}
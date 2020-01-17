package com.nibado.example.oauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
class OauthExamplesApplication {
	@Bean
	fun restTemplate() = RestTemplateBuilder().build()
}

fun main(args: Array<String>) {
	runApplication<OauthExamplesApplication>(*args)
}

package com.nibado.example.oauth.errorhandling

data class Problem(val title: String, val detail: String, val status: Int, val type: String? = null)

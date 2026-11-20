package com.example.myruijie

/**
 * Ruijie Cloud API integration.
 *
 * Official API authentication and endpoints must be added
 * after Ruijie provides the official documentation and access.
 */

data class RuijieRouter(
    val deviceId: String,
    val name: String,
    val model: String,
    val status: String
)

interface RuijieApiClient {
    suspend fun getRouters(): Result<List<RuijieRouter>>
}

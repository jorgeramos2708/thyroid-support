package com.jorgeramos.thyroidsupport.domain.repository

import kotlinx.coroutines.flow.Flow

data class OpenAIRequest(
    val prompt: String,
    val temperature: Double = 0.3,
    val maxTokens: Int = 500
)

data class OpenAIResponse(
    val content: String,
    val model: String = "gpt-3.5-turbo",
    val usage: Map<String, Int>? = null
)

interface OpenAIService {

    suspend fun chat(request: OpenAIRequest): OpenAIResponse

    fun streamChat(request: OpenAIRequest): Flow<String>
}

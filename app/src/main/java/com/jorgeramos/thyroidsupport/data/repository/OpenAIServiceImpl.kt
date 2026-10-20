package com.jorgeramos.thyroidsupport.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.jorgeramos.thyroidsupport.domain.repository.OpenAIRequest
import com.jorgeramos.thyroidsupport.domain.repository.OpenAIResponse
import com.jorgeramos.thyroidsupport.domain.repository.OpenAIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

class OpenAIServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OpenAIService {

    private val client = OkHttpClient()
    private val JSON = MediaType.get("application/json; charset=utf-8")

    override suspend fun chat(request: OpenAIRequest): OpenAIResponse = withContext(Dispatchers.IO) {
        val apiKey = getApiKey() ?: throw IllegalStateException("API Key de OpenAI no configurada")
        val body = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", listOf(
                JSONObject().apply {
                    put("role", "system")
                    put("content", "Eres un asistente de información sobre salud tiroidea. Responde en español. Siempre incluye descargo de responsabilidad: no eres médico, consulta a tu endocrinólogo.")
                },
                JSONObject().apply {
                    put("role", "user")
                    put("content", request.prompt)
                }
            ))
            put("temperature", request.temperature)
            put("max_tokens", request.maxTokens)
        }

        val req = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $apiKey")
            .post(RequestBody.create(body.toString(), JSON))
            .build()

        client.newCall(req).execute().use { response ->
            val responseBody = response.body?.string() ?: throw RuntimeException("Respuesta vacía de OpenAI")
            val json = JSONObject(responseBody)
            val content = json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
            OpenAIResponse(content = content)
        }
    }

    override fun streamChat(request: OpenAIRequest): Flow<String> = flow {
        // Implementación simplificada: no streaming en esta versión
        val response = chat(request)
        emit(response.content)
    }

    private fun getApiKey(): String? {
        // Leer de EncryptedSharedPreferences o DataStore cifrado
        // Implementación simplificada: leer de preferences
        val prefs = context.getSharedPreferences("thyroid_support_prefs", Context.MODE_PRIVATE)
        return prefs.getString("openai_api_key", null)
    }
}

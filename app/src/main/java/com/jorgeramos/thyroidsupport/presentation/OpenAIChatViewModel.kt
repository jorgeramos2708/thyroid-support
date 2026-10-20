package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.repository.OpenAIRequest
import com.jorgeramos.thyroidsupport.domain.repository.OpenAIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenAIChatViewModel @Inject constructor(
    private val openAIService: OpenAIService
) : ViewModel() {

    data class Message(
        val text: String,
        val isUser: Boolean,
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        // Mensaje de bienvenida
        _messages.value = listOf(
            Message(
                text = "Hola, soy tu asistente de información sobre salud tiroidea. Puedo responder preguntas sobre síntomas, interpretación de resultados de laboratorio y recomendaciones generales. Recuerda: no soy médico, consulta a tu endocrinólogo para decisiones de tratamiento.",
                isUser = false
            )
        )
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val userMsg = Message(text = text, isUser = true)
            _messages.value = (_messages.value ?: emptyList()) + userMsg

            try {
                val response = openAIService.chat(
                    OpenAIRequest(
                        prompt = text,
                        temperature = 0.3,
                        maxTokens = 500
                    )
                )
                val assistantMsg = Message(text = response.content, isUser = false)
                _messages.value = (_messages.value ?: emptyList()) + assistantMsg
            } catch (e: Exception) {
                _error.value = e.message
                val errorMsg = Message(
                    text = "Lo siento, hubo un error al procesar tu pregunta. ${e.message}",
                    isUser = false
                )
                _messages.value = (_messages.value ?: emptyList()) + errorMsg
            }
            _loading.value = false
        }
    }
}

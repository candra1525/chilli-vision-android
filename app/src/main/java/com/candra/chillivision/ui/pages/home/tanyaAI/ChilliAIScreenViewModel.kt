package com.candra.chillivision.ui.pages.home.tanyaAI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.model.ChatModel
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChilliAIScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    private val _conversation = MutableStateFlow<List<ChatModel.Message>>(emptyList())
    val conversation: StateFlow<List<ChatModel.Message>> get() = _conversation

    // LiveData untuk Loading
    val isLoading: StateFlow<Boolean>
        get() = _isLoading
    private val _isLoading = MutableStateFlow(false) // Tambahkan ini

    fun sendChat(msg: String) {
        val myChat = ChatModel.Message(msg, ChatModel.Author.me)

        viewModelScope.launch {
            _conversation.emit(_conversation.value + myChat)
            _isLoading.emit(true)  // ⬅️ Mulai loading

            try {
                val response = repository.getChatResponse(msg)
                _conversation.emit(
                    _conversation.value + ChatModel.Message(
                        response,
                        ChatModel.Author.bot
                    )
                )
            } catch (e: Exception) {
                _conversation.emit(
                    _conversation.value + ChatModel.Message(
                        "Error: ${e.message}",
                        ChatModel.Author.bot
                    )
                )
            } finally {
                _isLoading.emit(false) // ⬅️ Selesai loading
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            _conversation.emit(emptyList())
        }
    }

    fun getPreferences() = repository.getPreferences()

}

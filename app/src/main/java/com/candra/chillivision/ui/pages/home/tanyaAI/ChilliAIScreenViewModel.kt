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

    private val _countUsageAI = MutableStateFlow(0)
    val countUsageAI: StateFlow<Int> get() = _countUsageAI

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        // Ambil nilai awal countUsageAI dari preferences (Flow<String>)
        viewModelScope.launch {
            repository.getCountUsageAI().collect { stringCount ->
                val intCount = stringCount.toIntOrNull() ?: 0
                _countUsageAI.emit(intCount)
            }
        }
    }

    fun sendChat(msg: String) {
        val myChat = ChatModel.Message(msg, ChatModel.Author.me)

        viewModelScope.launch {
            _conversation.emit(_conversation.value + myChat)
            _isLoading.emit(true)

            try {
                val response = repository.getChatResponse(msg)

                _conversation.emit(
                    _conversation.value + ChatModel.Message(response, ChatModel.Author.bot)
                )

                // Ambil count saat ini dari flow
                val currentCount = _countUsageAI.value
                val updatedCount = currentCount + 1

                // Simpan ke preferences dan emit lagi ke flow
                repository.setCountUsageAI(updatedCount.toString())
                _countUsageAI.emit(updatedCount)

            } catch (e: Exception) {
                _conversation.emit(
                    _conversation.value + ChatModel.Message("Error: ${e.message}", ChatModel.Author.bot)
                )
            } finally {
                _isLoading.emit(false)
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

package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DownloadHistory
import com.example.data.network.TikWmApiService
import com.example.data.network.TikWmData
import com.example.data.repository.TikTokRepository
import com.example.utils.TikTokUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface VideoDetailState {
    object Idle : VideoDetailState
    object Loading : VideoDetailState
    data class Success(val data: TikWmData) : VideoDetailState
    data class Error(val message: String) : VideoDetailState
}

class TikTokViewModel(
    application: Application,
    private val repository: TikTokRepository
) : AndroidViewModel(application) {

    private val _inputUrl = MutableStateFlow("")
    val inputUrl: StateFlow<String> = _inputUrl.asStateFlow()

    private val _videoState = MutableStateFlow<VideoDetailState>(VideoDetailState.Idle)
    val videoState: StateFlow<VideoDetailState> = _videoState.asStateFlow()

    val downloadHistory: StateFlow<List<DownloadHistory>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onUrlChange(newUrl: String) {
        _inputUrl.value = newUrl
    }

    fun clearInput() {
        _inputUrl.value = ""
        _videoState.value = VideoDetailState.Idle
    }

    fun fetchVideo(url: String) {
        val extractedUrl = TikTokUtils.extractTikTokUrl(url) ?: url
        if (extractedUrl.isEmpty() || !TikTokUtils.isValidTikTokUrl(extractedUrl)) {
            _videoState.value = VideoDetailState.Error("Invalid TikTok URL. Please enter a valid link.")
            return
        }

        viewModelScope.launch {
            _videoState.value = VideoDetailState.Loading
            try {
                val response = repository.fetchVideoDetails(extractedUrl)
                if (response.code == 0 && response.data != null) {
                    val data = response.data
                    _videoState.value = VideoDetailState.Success(data)
                    
                    // Save to Room database history
                    val history = DownloadHistory(
                        tiktokId = data.id,
                        title = data.title ?: "TikTok Video",
                        authorUsername = data.author?.uniqueId ?: "unknown",
                        authorNickname = data.author?.nickname ?: "Unknown",
                        authorAvatarUrl = data.author?.avatarUrl ?: "",
                        coverUrl = data.cover ?: "",
                        videoUrl = data.play ?: "",
                        musicUrl = data.musicUrl ?: "",
                        duration = data.duration ?: 0,
                        size = data.size ?: 0L
                    )
                    repository.saveToHistory(history)
                } else {
                    _videoState.value = VideoDetailState.Error(response.msg.ifEmpty { "Failed to fetch video details." })
                }
            } catch (e: Exception) {
                _videoState.value = VideoDetailState.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun deleteHistoryItem(id: Int) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }

    class Factory(
        private val application: Application,
        private val repository: TikTokRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TikTokViewModel::class.java)) {
                return TikTokViewModel(application, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

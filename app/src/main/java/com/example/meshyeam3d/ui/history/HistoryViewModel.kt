package com.example.meshyeam3d.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meshyeam3d.data.model.HistoryItem
import com.example.meshyeam3d.data.repository.MeshyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val items: List<HistoryItem> = emptyList(),
    val downloadingId: String? = null,
    val message: String? = null,
    val modelFilePathToOpen: String? = null
)

class HistoryViewModel(
    private val repository: MeshyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState(items = repository.getHistory()))
    val uiState: StateFlow<HistoryUiState> = _uiState

    fun refresh() {
        _uiState.update { it.copy(items = repository.getHistory()) }
    }

    fun view3DModel(taskId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    downloadingId = taskId,
                    message = null,
                    modelFilePathToOpen = null
                )
            }
            repository.getOrDownloadModelFilePath(taskId)
                .onSuccess { filePath ->
                    _uiState.update {
                        it.copy(
                            downloadingId = null,
                            modelFilePathToOpen = filePath
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(downloadingId = null, message = error.message ?: "Download failed")
                    }
                }
        }
    }

    fun consumeModelOpen() {
        _uiState.update { it.copy(modelFilePathToOpen = null) }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    companion object {
        fun factory(repository: MeshyRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(repository) as T
                }
            }
    }
}

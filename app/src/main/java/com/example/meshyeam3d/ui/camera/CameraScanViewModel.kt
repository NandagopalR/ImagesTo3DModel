package com.example.meshyeam3d.ui.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meshyeam3d.data.repository.MeshyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CameraScanUiState(
    val images: List<Uri> = emptyList(),
    val isUploading: Boolean = false,
    val message: String? = null,
    val taskCreated: Boolean = false
)

class CameraScanViewModel(
    private val repository: MeshyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CameraScanUiState())
    val uiState: StateFlow<CameraScanUiState> = _uiState

    fun addImage(uri: Uri) {
        _uiState.update { state ->
            if (state.images.size >= REQUIRED_IMAGE_COUNT) {
                state.copy(message = "Only $REQUIRED_IMAGE_COUNT pictures needed")
            } else {
                state.copy(images = state.images + uri, message = null)
            }
        }
    }

    fun submit() {
        val images = _uiState.value.images
        if (images.size != REQUIRED_IMAGE_COUNT) {
            _uiState.update { it.copy(message = "Atleast $REQUIRED_IMAGE_COUNT pictures needed") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, message = null) }
            repository.createTask(images)
                .onSuccess {
                    _uiState.update {
                        it.copy(isUploading = false, taskCreated = true)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isUploading = false, message = error.message ?: "Upload failed")
                    }
                }
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    companion object {
        const val REQUIRED_IMAGE_COUNT = 4

        fun factory(repository: MeshyRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CameraScanViewModel(repository) as T
                }
            }
    }
}

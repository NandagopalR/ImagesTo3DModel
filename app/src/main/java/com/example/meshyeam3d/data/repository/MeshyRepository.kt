package com.example.meshyeam3d.data.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import com.example.meshyeam3d.BuildConfig
import com.example.meshyeam3d.data.local.HistoryPreferences
import com.example.meshyeam3d.data.model.CreateTaskRequest
import com.example.meshyeam3d.data.model.HistoryItem
import com.example.meshyeam3d.data.model.MeshyTaskResponse
import com.example.meshyeam3d.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class MeshyRepository(
    private val context: Context,
    private val historyPreferences: HistoryPreferences
) {
    private val downloadClient = OkHttpClient()
    private val api = RetrofitClient.instance

    suspend fun createTask(imageUris: List<Uri>): Result<HistoryItem> =
        withContext(Dispatchers.IO) {
            runCatching {
                val imageDataUris = imageUris.take(MESHY_MAX_IMAGES).map { uri -> uri.toDataUri() }
                val apiKey = "Bearer ${BuildConfig.MESHY_API_KEY}"
                val response =
                    api.createTask(CreateTaskRequest(imageUrls = imageDataUris), apiKey = apiKey)
                val item = HistoryItem(id = response.result, createdAt = System.currentTimeMillis())
                historyPreferences.addHistory(item)
                item
            }
        }

    fun getHistory(): List<HistoryItem> = historyPreferences.getHistory()

    suspend fun getTask(id: String): Result<MeshyTaskResponse> = withContext(Dispatchers.IO) {
        val apiKey = "Bearer ${BuildConfig.MESHY_API_KEY}"
        runCatching { api.getTask(id, apiKey = apiKey) }
    }

    fun getModelFilePath(taskId: String): String {
        return File(getModelDirectory(), "$taskId.glb").absolutePath
    }

    fun isModelFileAvailable(taskId: String): Boolean {
        val file = File(getModelFilePath(taskId))
        return file.exists() && file.length() > 0
    }

    suspend fun getOrDownloadModelFilePath(taskId: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            if (isModelFileAvailable(taskId)) return@runCatching getModelFilePath(taskId)

            val apiKey = "Bearer ${BuildConfig.MESHY_API_KEY}"
            val task = api.getTask(taskId, apiKey)
            val modelUrl = task.modelUrls?.get("glb")
                ?: error("GLB model is not ready yet. Current status: ${task.status.orEmpty()}")
            val request = Request.Builder().url(modelUrl).build()
            val response = downloadClient.newCall(request).execute()
            if (!response.isSuccessful) error("Download failed: ${response.code}")
            val body = response.body ?: error("Empty download response")

            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "$taskId.glb")
                put(MediaStore.Downloads.MIME_TYPE, "model/gltf-binary")
                put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    "${Environment.DIRECTORY_DOWNLOADS}/Meshy_EAM"
                )
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: error("Unable to create download file")
            resolver.openOutputStream(uri)?.use { output ->
                body.byteStream().use { input -> input.copyTo(output) }
            } ?: error("Unable to write download file")

            val completedValues = ContentValues().apply {
                put(MediaStore.Downloads.IS_PENDING, 0)
            }
            resolver.update(uri, completedValues, null, null)
            getModelFilePath(taskId)
        }
    }

    private fun Uri.toDataUri(): String {
        val bytes = context.contentResolver.openInputStream(this)?.use { it.readBytes() }
            ?: error("Unable to read image")
        val encoded = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return "data:image/jpeg;base64,$encoded"
    }

    private fun getModelDirectory(): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Meshy_EAM"
        )
    }

    companion object {
        private const val MESHY_MAX_IMAGES = 4
    }
}

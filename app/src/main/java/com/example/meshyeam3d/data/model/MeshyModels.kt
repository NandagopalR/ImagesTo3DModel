package com.example.meshyeam3d.data.model

import com.google.gson.annotations.SerializedName

data class CreateTaskRequest(
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("should_texture") val shouldTexture: Boolean = true,
    @SerializedName("enable_pbr") val enablePbr: Boolean = true,
    @SerializedName("target_formats") val targetFormats: List<String> = listOf("glb")
)

data class CreateTaskResponse(
    val result: String
)

data class MeshyTaskResponse(
    val id: String,
    val status: String?,
    val progress: Int?,
    @SerializedName("created_at") val createdAt: Long?,
    @SerializedName("model_urls") val modelUrls: Map<String, String>?,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?
)

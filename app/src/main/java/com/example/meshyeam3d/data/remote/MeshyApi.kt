package com.example.meshyeam3d.data.remote

import com.example.meshyeam3d.data.model.CreateTaskRequest
import com.example.meshyeam3d.data.model.CreateTaskResponse
import com.example.meshyeam3d.data.model.MeshyTaskResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface MeshyApi {
    @POST("openapi/v1/multi-image-to-3d")
    suspend fun createTask(
        @Body request: CreateTaskRequest,
        @Header("Authorization") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): CreateTaskResponse

    @GET("openapi/v1/multi-image-to-3d/{id}")
    suspend fun getTask(
        @Path("id") id: String,
        @Header("Authorization") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): MeshyTaskResponse
}

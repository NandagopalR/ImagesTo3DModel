package com.example.meshyeam3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.meshyeam3d.data.local.HistoryPreferences
import com.example.meshyeam3d.data.remote.MeshyApiFactory
import com.example.meshyeam3d.data.repository.MeshyRepository
import com.example.meshyeam3d.ui.navigation.MeshyNavHost
import com.example.meshyeam3d.ui.theme.MeshyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = MeshyRepository(
            context = applicationContext,
            historyPreferences = HistoryPreferences(applicationContext)
        )

        setContent {
            MeshyTheme {
                MeshyNavHost(repository = repository)
            }
        }
    }
}

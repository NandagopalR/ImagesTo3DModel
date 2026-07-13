package com.example.meshyeam3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.meshyeam3d.data.local.HistoryPreferences
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
                Scaffold(Modifier.fillMaxSize()) { paddingValues ->
                    MeshyNavHost(
                        modifier = Modifier.padding(paddingValues),
                        repository = repository
                    )
                }
            }
        }
    }
}

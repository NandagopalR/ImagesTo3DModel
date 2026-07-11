package com.example.meshyeam3d.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0D1321),
    secondary = Color(0xFF4CC9F0),
    tertiary = Color(0xFFFFB703),
    background = Color(0xFFF8FAFC),
    surface = Color.White
)

@Composable
fun MeshyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}

package com.example.meshyeam3d.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meshyeam3d.data.repository.MeshyRepository
import com.example.meshyeam3d.ui.camera.CameraScanScreen
import com.example.meshyeam3d.ui.camera.CameraScanViewModel
import com.example.meshyeam3d.ui.history.HistoryScreen
import com.example.meshyeam3d.ui.history.HistoryViewModel
import com.example.meshyeam3d.ui.home.HomeScreen
import com.example.meshyeam3d.ui.splash.SplashScreen

@Composable
fun MeshyNavHost(repository: MeshyRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Splash.path) {
        composable(Route.Splash.path) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Home.path) {
            HomeScreen(
                onCreateModel = { navController.navigate(Route.Camera.path) },
                onHistory = { navController.navigate(Route.History.path) }
            )
        }
        composable(Route.Camera.path) {
            val viewModel: CameraScanViewModel = viewModel(
                factory = CameraScanViewModel.factory(repository)
            )
            CameraScanScreen(
                viewModel = viewModel,
                onTaskCreated = {
                    navController.navigate(Route.History.path) {
                        popUpTo(Route.Home.path)
                    }
                }
            )
        }
        composable(Route.History.path) {
            val viewModel: HistoryViewModel = viewModel(
                factory = HistoryViewModel.factory(repository)
            )
            HistoryScreen(viewModel = viewModel)
        }
    }
}

private sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Home : Route("home")
    data object Camera : Route("camera")
    data object History : Route("history")
}

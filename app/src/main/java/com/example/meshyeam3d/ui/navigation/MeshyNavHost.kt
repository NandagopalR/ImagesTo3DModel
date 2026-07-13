package com.example.meshyeam3d.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.meshyeam3d.data.repository.MeshyRepository
import com.example.meshyeam3d.ui.camera.CameraScanScreen
import com.example.meshyeam3d.ui.camera.CameraScanViewModel
import com.example.meshyeam3d.ui.history.HistoryScreen
import com.example.meshyeam3d.ui.history.HistoryViewModel
import com.example.meshyeam3d.ui.home.HomeScreen
import com.example.meshyeam3d.ui.splash.SplashScreen
import com.example.meshyeam3d.ui.view3dmodel.View3DModelScreen

@Composable
fun MeshyNavHost(repository: MeshyRepository, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash.path,
        modifier = modifier
    ) {
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
            HistoryScreen(
                viewModel = viewModel,
                onOpenModel = { filePath ->
                    navController.navigate(Route.View3DModel.createPath(filePath))
                }
            )
        }
        composable(
            route = Route.View3DModel.path,
            arguments = listOf(navArgument(Route.View3DModel.ARG_FILE_PATH) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val filePath = backStackEntry.arguments
                ?.getString(Route.View3DModel.ARG_FILE_PATH)
                .orEmpty()
            View3DModelScreen(
                filePath = filePath,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Home : Route("home")
    data object Camera : Route("camera")
    data object History : Route("history")
    data object View3DModel : Route("view_3d_model/{filePath}") {
        const val ARG_FILE_PATH = "filePath"

        fun createPath(filePath: String): String = "view_3d_model/${Uri.encode(filePath)}"
    }
}

package com.averyvi.bilbo

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.averyvi.bilbo.notui.FirstHarmonic
import com.averyvi.bilbo.notui.SelectableBluetoothDevice
import com.averyvi.bilbo.ui.app.AboutScreen
import com.averyvi.bilbo.ui.app.InstrumentSelectScreen
import com.averyvi.bilbo.ui.app.IntroScreen
import com.averyvi.bilbo.ui.app.NowPlayingScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppUI(
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
    onHarmonicSelected: (FirstHarmonic) -> Unit
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.CurrentlyPlaying.name,
    ){

        val onRouteButtonClicked = { route: Routes ->
            navController.navigate(route = route.name)
        }

        composable(route = Routes.CurrentlyPlaying.name){
            NowPlayingScreen(
                onRouteButtonClicked = onRouteButtonClicked,
                deviceList = deviceList,
                onDeviceSelected = onDeviceSelected
            )
        }
        composable(route = Routes.InstrumentSelect.name){
            InstrumentSelectScreen(onRouteButtonClicked = onRouteButtonClicked)
        }
        composable(route = Routes.About.name) {
            AboutScreen(onRouteButtonClicked = onRouteButtonClicked)
        }
        composable(route = Routes.Intro.name) {
            IntroScreen(onRouteButtonClicked = onRouteButtonClicked)
        }
    }
}

enum class Routes(
    @StringRes val title: Int,
){
    Intro(
        title = R.string.IntroScreen
    ),
    CurrentlyPlaying(
        title = R.string.CurrentlyPlaying
    ),
    InstrumentSelect(
        title = R.string.InstrumentSelectScreen
    ),
    About(
        title = R.string.HelpScreen
    )
}
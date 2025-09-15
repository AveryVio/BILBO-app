package com.averyvi.bilbo.appuis

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.averyvi.bilbo.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppUI(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.CurrentlyPlaying.name,
    ){
        val onHelpButtonClicked = {
            navController.navigate(route = Routes.Help.name)
        }
        val onInstrumentButtonClicked = {
            navController.navigate(route = Routes.InstrumentSelect.name)
        }
        val onCurrentlyPlayingClicked = {
            navController.navigate(route = Routes.CurrentlyPlaying.name)
        }

        composable(route = Routes.CurrentlyPlaying.name){
            NowPlayingScreen(
                onHelpButtonClicked = onHelpButtonClicked,
                onInstrumentButtonClicked = onInstrumentButtonClicked,
                onCurrentlyPlayingClicked = onCurrentlyPlayingClicked
            )
        }
        composable(route = Routes.InstrumentSelect.name){
            InstrumentSelectScreen(
                onHelpButtonClicked = onHelpButtonClicked,
                onInstrumentButtonClicked = onInstrumentButtonClicked,
                onCurrentlyPlayingClicked = onCurrentlyPlayingClicked
            )
        }
        composable(route = Routes.Help.name) {
            HelpScreen(
                onHelpButtonClicked = onHelpButtonClicked,
                onInstrumentButtonClicked = onInstrumentButtonClicked,
                onCurrentlyPlayingClicked = onCurrentlyPlayingClicked
            )
        }
        composable(route = Routes.Intro.name) {
            IntroScreen(
                onNextScreenButtonClicked = {
                    navController.navigate(route = Routes.InstrumentSelect.name)
                }
            )
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
    Help(
        title = R.string.HelpScreen
    )
}
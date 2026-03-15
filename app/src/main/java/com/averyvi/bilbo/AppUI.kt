package com.averyvi.bilbo

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.averyvi.bilbo.definitions.FirstHarmonic
import com.averyvi.bilbo.definitions.MusicalNote
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.storage.AppDatabase
import com.averyvi.bilbo.ui.app.AboutScreen
import com.averyvi.bilbo.ui.app.InstrumentSelectScreen
import com.averyvi.bilbo.ui.app.IntroScreen
import com.averyvi.bilbo.ui.app.NowPlayingScreen
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppUI(
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
    onHarmonicSelected: (FirstHarmonic) -> Unit
){
    val locContext = LocalContext.current
    val db = Room.databaseBuilder(
        locContext,
        AppDatabase::class.java, "instruments"
    ).build()
    val userDao = db.userDao()

    val note = remember { mutableIntStateOf(0) }
    val octive = remember { mutableIntStateOf(4) }

    val pitch = remember {
        mutableIntStateOf(0)
    }
    val pitchStep = remember {
        mutableDoubleStateOf(0.0)
    }
    LaunchedEffect(Unit) {
        while(true) {
            pitchStep.doubleValue += 0.1
            pitch.value = (sin(pitchStep.doubleValue) * 100).toInt()
            delay(50)
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            if(note.value >= 6) note.value = 0
            else note.value++

            if(octive.value >= 8) octive.value = -2
            else octive.value++

            delay(500)
        }
    }

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.InstrumentSelect.name,
    ){

        val onRouteButtonClicked = { route: Routes ->
            navController.navigate(route = route.name)
        }

        composable(route = Routes.CurrentlyPlaying.name){
            NowPlayingScreen(
                note = MusicalNote.entries[note.intValue].name,
                octive = octive.intValue.toString(),
                pitch = pitch,
                onRouteButtonClicked = onRouteButtonClicked,
                deviceList = deviceList,
                onDeviceSelected = onDeviceSelected
            )
        }
        composable(route = Routes.InstrumentSelect.name){
            InstrumentSelectScreen(
                note = MusicalNote.entries[note.value].name,
                octive = octive.intValue.toString(),
                pitch = pitch,
                dbDao = userDao,
                onRouteButtonClicked = onRouteButtonClicked
            )
        }
        composable(route = Routes.About.name) {
            AboutScreen(
                note = MusicalNote.entries[note.intValue].name,
                octive = octive.intValue.toString(),
                pitch = pitch,
                onRouteButtonClicked = onRouteButtonClicked
            )
        }
        composable(route = Routes.Intro.name) {
            IntroScreen(onRouteButtonClicked = onRouteButtonClicked)
        }
    }
}

enum class Routes(
    @field:StringRes val title: Int,
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
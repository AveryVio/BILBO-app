package com.averyvi.bilbo

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.averyvi.bilbo.definitions.FirstHarmonic
import com.averyvi.bilbo.definitions.InstrumentStyling
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.storage.AppDatabase
import com.averyvi.bilbo.ui.app.NewInstrumentScreen
import com.averyvi.bilbo.ui.app.InstrumentSelectScreen
import com.averyvi.bilbo.ui.app.IntroScreen
import com.averyvi.bilbo.ui.app.MainScaffold
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

    val noteN = remember { mutableIntStateOf(0) }
    val note = remember { mutableStateOf("A") }
    val octiveN = remember { mutableIntStateOf(4) }
    val octive = remember { mutableStateOf("4") }

    val pitch = remember {
        mutableIntStateOf(0)
    }
    val pitchStep = remember {
        mutableDoubleStateOf(0.0)
    }
    LaunchedEffect(Unit) {
        while(true) {
            pitchStep.doubleValue += 0.1
            pitch.intValue = (sin(pitchStep.doubleValue) * 100).toInt()
            delay(50)
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            if(noteN.intValue >= 6) noteN.intValue = 0
            else noteN.intValue++
            note.value = noteN.intValue.toString()

            if(octiveN.intValue >= 8) octiveN.intValue = -2
            else octiveN.intValue++
            octive.value = octiveN.intValue.toString()

            delay(500)
        }
    }

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithoutBars = listOf(
        Routes.Intro.name
    )
    val showBars = currentRoute !in routesWithoutBars
    val showNewInstrumentbutton = currentRoute == Routes.InstrumentSelect.name

    val defaultRouteToGoTo = mapOf<String, String>(
        Routes.InstrumentSelect.name to Routes.CurrentlyPlaying.name,
        Routes.CurrentlyPlaying.name to Routes.InstrumentSelect.name,
        Routes.NewInstrument.name to Routes.InstrumentSelect.name,
    )
    val secondaryRouteToGoTo = mapOf<String, String>(
        Routes.Intro.name to Routes.NewInstrument.name,
        Routes.InstrumentSelect.name to Routes.NewInstrument.name,
        Routes.CurrentlyPlaying.name to Routes.Intro.name,
        Routes.NewInstrument.name to Routes.CurrentlyPlaying.name,
    )
    val defaultRouteDestination = defaultRouteToGoTo.getOrDefault(currentRoute, Routes.InstrumentSelect.name)
    val secondaryRouteDestination = secondaryRouteToGoTo.getOrDefault(currentRoute, Routes.CurrentlyPlaying.name)

    MainScaffold(
        selectedInstrumentStyling = InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon),
        note = note,
        octive = octive,
        pitch = pitch,
        showNewInstrumentButton = showNewInstrumentbutton,
        showTopBar = showBars,
        showBottomBar = showBars,
        onRouteButtonClicked = { alternateRoute ->
            if(alternateRoute) navController.navigate(secondaryRouteDestination)
            else navController.navigate(defaultRouteDestination)
                               },
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.NewInstrument.name,
        ) {

            val onRouteButtonClicked = { route: Routes ->
                navController.navigate(route = route.name)
            }

            composable(route = Routes.CurrentlyPlaying.name) {
                NowPlayingScreen(
                    note = note,
                    octive = octive,
                    pitch = pitch,
                    deviceList = deviceList,
                    onDeviceSelected = onDeviceSelected,
                )
            }
            composable(route = Routes.InstrumentSelect.name) {
                InstrumentSelectScreen(
                    dbDao = userDao
                )
            }
            composable(route = Routes.NewInstrument.name) {
                NewInstrumentScreen(
                    onRouteButtonClicked = onRouteButtonClicked,
                    dbDao = userDao
                )
            }
            composable(route = Routes.Intro.name) {
                IntroScreen(
                    onRouteButtonClicked = onRouteButtonClicked
                )
            }
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
    NewInstrument(
        title = R.string.NewInstrumentScreen
    )
}
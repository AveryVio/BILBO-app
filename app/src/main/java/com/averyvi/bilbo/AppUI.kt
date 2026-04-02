package com.averyvi.bilbo

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.averyvi.bilbo.definitions.BottomButton
import com.averyvi.bilbo.definitions.FirstHarmonic
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.data.storage.AppDatabase
import com.averyvi.bilbo.data.uiState.InstrumentProfileViewModel
import com.averyvi.bilbo.definitions.AppBarsVisibility
import com.averyvi.bilbo.ui.app.NewInstrumentScreen
import com.averyvi.bilbo.ui.app.InstrumentSelectScreen
import com.averyvi.bilbo.ui.app.IntroScreen
import com.averyvi.bilbo.ui.app.MainScaffold
import com.averyvi.bilbo.ui.app.NowPlayingScreen
import com.averyvi.bilbo.data.uiState.TuningViewModel
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppUI(
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
    onHarmonicSelected: (FirstHarmonic) -> Unit
){
    val instrumentProfileViewModel: InstrumentProfileViewModel = viewModel()
    val tuningViewModel: TuningViewModel = viewModel()

    val locContext = LocalContext.current
    val db = Room.databaseBuilder(
        locContext,
        AppDatabase::class.java, "instruments"
    ).build()
    val userDao = db.userDao()

    val pitchStep = remember {
        mutableDoubleStateOf(0.0)
    }
    LaunchedEffect(Unit) {
        while(true) {
            pitchStep.doubleValue += 0.1
            tuningViewModel.updatePitch((sin(pitchStep.doubleValue) * 100).toInt())
            delay(50)
        }
    }
    LaunchedEffect(Unit) {
        while(true) {
            if(tuningViewModel.note.value.ordinal >= 6) tuningViewModel.resetNote()
            else tuningViewModel.incrementNote()

            if(tuningViewModel.octive.value >= 8) tuningViewModel.resetOctive()
            else tuningViewModel.incrementOctive()

            delay(500)
        }
    }

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val appBarsVisibility = mapOf<String, AppBarsVisibility>(
        Routes.Intro.name to AppBarsVisibility.neither,
        Routes.InstrumentSelect.name to AppBarsVisibility.both,
        Routes.CurrentlyPlaying.name to AppBarsVisibility.both,
        Routes.NewInstrument.name to AppBarsVisibility.bottom,
    )
    val appBarsShow = appBarsVisibility.getOrDefault(currentRoute, AppBarsVisibility.both)

    val defaultRouteToGoTo = mapOf<String, String>(
        Routes.InstrumentSelect.name to Routes.CurrentlyPlaying.name,
        Routes.CurrentlyPlaying.name to Routes.InstrumentSelect.name,
        Routes.NewInstrument.name to Routes.CurrentlyPlaying.name,
    )
    val secondaryRouteToGoTo = mapOf<String, String>(
        Routes.Intro.name to Routes.NewInstrument.name,
        Routes.InstrumentSelect.name to Routes.NewInstrument.name,
        Routes.CurrentlyPlaying.name to Routes.Intro.name,
        Routes.NewInstrument.name to Routes.InstrumentSelect.name,
    )
    val defaultRouteDestination = defaultRouteToGoTo.getOrDefault(currentRoute, Routes.InstrumentSelect.name)
    val secondaryRouteDestination = secondaryRouteToGoTo.getOrDefault(currentRoute, Routes.CurrentlyPlaying.name)

    val bottomButtonSeeSettings = mapOf<String, BottomButton>(
        Routes.Intro.name to BottomButton.nul,
        Routes.InstrumentSelect.name to BottomButton.new,
        Routes.CurrentlyPlaying.name to BottomButton.nul,
        Routes.NewInstrument.name to BottomButton.add,
    )
    val bottomButtonViewable = bottomButtonSeeSettings.getOrDefault(currentRoute, BottomButton.nul)

    MainScaffold(
        instrumentProfileViewModel = instrumentProfileViewModel,
        tuningViewModel = tuningViewModel,
        dbDao = userDao,
        bottomButton = bottomButtonViewable,
        barsVisibility = appBarsShow,
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
                    tuningViewModel = tuningViewModel,
                    deviceList = deviceList,
                    onDeviceSelected = onDeviceSelected,
                )
            }
            composable(route = Routes.InstrumentSelect.name) {
                InstrumentSelectScreen(
                    dbDao = userDao,
                    instrumentProfileViewModel = instrumentProfileViewModel,
                )
            }
            composable(route = Routes.NewInstrument.name) {
                NewInstrumentScreen(
                    instrumentProfileViewModel = instrumentProfileViewModel,
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
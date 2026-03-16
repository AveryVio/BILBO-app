package com.averyvi.bilbo.ui.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.InstrumentStyling
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.storage.UserDao

@Composable
fun NowPlayingScreen(
    note: State<String>,
    octive: State<String>,
    pitch: MutableState<Int>,
    onRouteButtonClicked: (Routes) -> Unit,
    defaultRouteDestination: Routes,
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        note = note,
        octive = octive,
        pitch = pitch,
        showNewInstrumentButton = false,
        onRouteButtonClicked = onRouteButtonClicked,
        defaultRouteDestination = defaultRouteDestination
    ) { innerPadding ->
        NowPlayingScreenContents(
            paddingValues =  innerPadding,
            note = note,
            octive = octive,
            pitch = pitch,
            deviceList = deviceList,
            onDeviceSelected = onDeviceSelected,
            )
    }
}

@Composable
fun InstrumentSelectScreen(
    note: State<String>,
    octive: State<String>,
    pitch: MutableState<Int>,
    dbDao: UserDao,
    onRouteButtonClicked: (Routes) -> Unit,
    defaultRouteDestination: Routes,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        note = note,
        octive = octive,
        pitch = pitch,
        showNewInstrumentButton = true,
        onRouteButtonClicked = onRouteButtonClicked,
        defaultRouteDestination = defaultRouteDestination
    ) { innerPadding ->
        InstrumentSelectScreenContents(
            paddingValues =  innerPadding,
        )
    }
}

@Composable
fun AboutScreen(
    note: State<String>,
    octive: State<String>,
    pitch: MutableState<Int>,
    onRouteButtonClicked: (Routes) -> Unit,
    defaultRouteDestination: Routes,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        note = note,
        octive = octive,
        pitch = pitch,
        showNewInstrumentButton = false,
        onRouteButtonClicked = onRouteButtonClicked,
        defaultRouteDestination = defaultRouteDestination
    ) { innerPadding ->
        AboutScreenContents(
            paddingValues =  innerPadding,
            onRouteButtonClicked = onRouteButtonClicked,
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IntroScreen(
    onRouteButtonClicked: (Routes) -> Unit,
    defaultRouteDestination: Routes,
){
    Surface(Modifier.fillMaxSize()) {
        IntroScreenContents(
            onRouteButtonClicked = onRouteButtonClicked
        )
    }
}
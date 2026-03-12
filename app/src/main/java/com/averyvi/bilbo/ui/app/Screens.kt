package com.averyvi.bilbo.ui.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.notui.FirstHarmonic
import com.averyvi.bilbo.notui.InstrumentStyling
import com.averyvi.bilbo.notui.SelectableBluetoothDevice

@Composable
fun NowPlayingScreen(
    note: String,
    octive: String,
    pitch: MutableState<Int>,
    onRouteButtonClicked: (Routes) -> Unit,
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onRouteButtonClicked = onRouteButtonClicked,
    ) { innerPadding ->
        NowPlayingScreenContents(
            paddingValues =  innerPadding,
            note = note,
            octive = octive,
            pitch = pitch,
            onRouteButtonClicked = onRouteButtonClicked,
            deviceList = deviceList,
            onDeviceSelected = onDeviceSelected
            )
    }
}

@Composable
fun InstrumentSelectScreen(
    note: String,
    octive: String,
    pitch: MutableState<Int>,
    onRouteButtonClicked: (Routes) -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onRouteButtonClicked = onRouteButtonClicked,
    ) { innerPadding ->
        InstrumentSelectScreenContents(
            paddingValues =  innerPadding,
            note = note,
            octive = octive,
            pitch = pitch,
            onRouteButtonClicked = onRouteButtonClicked,
        )
    }
}

@Composable
fun AboutScreen(
    note: String,
    octive: String,
    pitch: MutableState<Int>,
    onRouteButtonClicked: (Routes) -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onRouteButtonClicked = onRouteButtonClicked,
    ) { innerPadding ->
        AboutScreenContents(
            paddingValues =  innerPadding,
            pitch = pitch,
            onRouteButtonClicked = onRouteButtonClicked,
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IntroScreen(
    onRouteButtonClicked: (Routes) -> Unit,
){
    Surface(Modifier.fillMaxSize()) {
        IntroScreenContents(onRouteButtonClicked = onRouteButtonClicked)
    }
}
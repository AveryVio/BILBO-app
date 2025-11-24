package com.averyvi.bilbo.ui.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.notui.FirstHarmonic
import com.averyvi.bilbo.notui.InstrumentStyling
import com.averyvi.bilbo.notui.SelectableBluetoothDevice

@Composable
fun NowPlayingScreen(
    onRouteButtonClicked: (Routes) -> Unit,
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
    onHarmonicSelected: (FirstHarmonic) -> Unit
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onRouteButtonClicked = onRouteButtonClicked,
    ) { innerPadding ->
        NowPlayingScreenContents(
            paddingValues =  innerPadding,
            deviceList = deviceList,
            onDeviceSelected = onDeviceSelected,
            onHarmonicSelected = onHarmonicSelected
            )
    }
}

@Composable
fun InstrumentSelectScreen(
    onRouteButtonClicked: (Routes) -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onRouteButtonClicked = onRouteButtonClicked,
    ) { innerPadding ->
        InstrumentSelectScreenContents(innerPadding)
    }
}

@Composable
fun HelpScreen(
    onRouteButtonClicked: (Routes) -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onRouteButtonClicked = onRouteButtonClicked,
    ) { innerPadding ->
        HelpScreenContents(innerPadding)
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
package com.averyvi.bilbo.appuis

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.averyvi.bilbo.R
import com.averyvi.bilbo.notuis.InstrumentStyling

@Composable
fun NowPlayingScreen(
    onHelpButtonClicked: () -> Unit,
    onInstrumentButtonClicked: () -> Unit,
    onCurrentlyPlayingClicked: () -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onHelpButtonClicked = onHelpButtonClicked,
        onInstrumentButtonClicked = onInstrumentButtonClicked,
        onCurrentlyPlayingClicked = onCurrentlyPlayingClicked,
    ) { innerPadding ->
        NowPlayingScreenContents(innerPadding)
    }
}

@Composable
fun InstrumentSelectScreen(
    onHelpButtonClicked: () -> Unit,
    onInstrumentButtonClicked: () -> Unit,
    onCurrentlyPlayingClicked: () -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onHelpButtonClicked = onHelpButtonClicked,
        onInstrumentButtonClicked = onInstrumentButtonClicked,
        onCurrentlyPlayingClicked = onCurrentlyPlayingClicked,
    ) { innerPadding ->
        InstrumentSelectScreenContents(innerPadding)
    }
}

@Composable
fun HelpScreen(
    onHelpButtonClicked: () -> Unit,
    onInstrumentButtonClicked: () -> Unit,
    onCurrentlyPlayingClicked: () -> Unit,
){
    MainScaffold(
        modifier = Modifier.fillMaxSize(),
        selectedInstrumentStyling =  InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red),
        onHelpButtonClicked = onHelpButtonClicked,
        onInstrumentButtonClicked = onInstrumentButtonClicked,
        onCurrentlyPlayingClicked = onCurrentlyPlayingClicked,
    ) { innerPadding ->
        HelpScreenContents(innerPadding)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IntroScreen(
    onNextScreenButtonClicked: () -> Unit
){
    Surface(Modifier.fillMaxSize()) {
        IntroScreenContents(onNextScreenButtonClicked)
    }
}
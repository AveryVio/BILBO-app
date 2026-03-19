package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.InstrumentStyling
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.ui.fragments.DeviceList
import com.averyvi.bilbo.ui.fragments.InstrumentShelfItem
import com.averyvi.bilbo.ui.fragments.IntroAppTitle
import com.averyvi.bilbo.ui.fragments.IntroTutorial
import com.averyvi.bilbo.ui.fragments.PitchDiffView

@Composable
fun NowPlayingScreen(
    note: State<String>,
    octive: State<String>,
    pitch: MutableState<Int>,
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
){
    Box(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ){
                Box(
                    modifier = Modifier
                        .height(150.dp),
                    contentAlignment = Alignment.BottomCenter
                ){
                    Text(
                        text = "placeholder",
                        fontSize = 45.sp,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    )
                }
                PitchDiffView(pitch)
            }
        }

        DeviceList(
            devices = deviceList,
            onDeviceSelected = onDeviceSelected
        )
    }
}

@Composable
fun InstrumentSelectScreen(
    //dbDao: UserDao,
){
    //val instruments: Flow<List<InstrumentDBRow>>


    val a = InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.radio_button_checked_24px, instrumentThemeColor = Color.Red)
    val e: MutableList<InstrumentStyling> = MutableList(size = 6) {
        InstrumentStyling(
            instrumentName = "Piano",
            instrumentIcon = R.drawable.androidicon,
            instrumentThemeColor = Color.Red
        )
    };

    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.radio_button_checked_24px, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.handa, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.ic_launcher_foreground, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.cupcake, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(top = 6.dp),
            columns = GridCells.Adaptive(75.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(e.size) {
                InstrumentShelfItem(
                    InstrumentImageResource = e[it].instrumentIcon,
                    name = e[it].instrumentName,
                )
            }
        }
    }
}

@Composable
fun AboutScreen(
    onRouteButtonClicked: (Routes) -> Unit,
){
    val a = InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.size(233.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Row {
            Text(
                text = stringResource(R.string.AppName),
                fontWeight = FontWeight(400),
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "•",
                fontWeight = FontWeight(800),
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "version:" + System.getProperty("jpackage.app-version"),
                fontWeight = FontWeight(400),
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            Text(text = stringResource(R.string.MostUsedInstrumentsStatName))
            Row {
                InstrumentShelfItem(
                    InstrumentImageResource = a.instrumentIcon,
                    name = a.instrumentName,
                )
                InstrumentShelfItem(
                    InstrumentImageResource = a.instrumentIcon,
                    name = a.instrumentName,
                )
                InstrumentShelfItem(
                    InstrumentImageResource = a.instrumentIcon,
                    name = a.instrumentName,
                )
            }
        }

        //add links to github
    }
}

@Composable
fun IntroScreen(onRouteButtonClicked: (Routes) -> Unit) {
    Surface(modifier = Modifier
        .statusBarsPadding()
        .fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            IntroAppTitle()

            IntroTutorial()

            /* add some fluff */
            /*
            github
            licences/ licence
             */
            Button(
                onClick = { onRouteButtonClicked(Routes.InstrumentSelect) }
            ) {
                Text(
                    text = stringResource(R.string.nextButton),
                    fontWeight = FontWeight(500),
                    fontSize = 25.sp,
                )
            }
        }
    }
}
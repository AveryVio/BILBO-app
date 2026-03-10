package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.handwriting.handwritingDetector
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.notui.InstrumentStyling
import com.averyvi.bilbo.notui.SelectableBluetoothDevice
import com.averyvi.bilbo.ui.fragments.DeviceList
import com.averyvi.bilbo.ui.fragments.FilterRangeDisplay
import com.averyvi.bilbo.ui.fragments.InstrumentShelfItem
import com.averyvi.bilbo.ui.fragments.IsHarmonicText
import com.averyvi.bilbo.ui.fragments.NoteOctiveDisplay
import com.averyvi.bilbo.ui.fragments.PitchDiffView
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun NowPlayingScreenContents(
    paddingValues: PaddingValues,
    deviceList: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit,
){
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 100.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            val pitch = remember {
                mutableIntStateOf(0)
            }
            val pitchStep = remember {
                mutableDoubleStateOf(0.0)
            }
            LaunchedEffect (Unit) {
                pitch.value = (sin(0.4) * 100).toInt()
                while(true) {
                    pitchStep.doubleValue += 0.1
                    pitch.value = (sin(pitchStep.doubleValue) * 100).toInt()
                    delay(50)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy( 10.dp)
            ){
                Text(
                    text = "placeholder",
                    fontSize = 45.sp,
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle
                )
                PitchDiffView(pitch)
            }


            Card(
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NoteOctiveDisplay("A", 4.toString())
                        VerticalDivider(
                            modifier = Modifier.height(5.dp),
                            thickness = 5.dp
                        )
                        IsHarmonicText(pitch)
                        VerticalDivider(
                            modifier = Modifier.height(5.dp),
                            thickness = 5.dp
                        )
                        FilterRangeDisplay(3)
                    }
                }
            }
        }

        DeviceList(
            devices = deviceList,
            onDeviceSelected = onDeviceSelected
        )
    }
}

@Composable
fun InstrumentSelectScreenContents(paddingValues: PaddingValues){
    val a = InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.radio_button_checked_24px, instrumentThemeColor = Color.Red)
    val e: MutableList<InstrumentStyling> = MutableList(size = 6, {InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red)} );

    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.radio_button_checked_24px, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.handa, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.ic_launcher_foreground, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.cupcake, instrumentThemeColor = Color.Red))
    e.addLast(InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.bluetooth_searching, instrumentThemeColor = Color.Red))

    val columnCount = 4

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top  = 6.dp),
        columns = GridCells.Fixed(4),
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

@Composable
fun AboutScreenContents(paddingValues: PaddingValues){
    val a = InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon, instrumentThemeColor = Color.Red)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
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
fun IntroScreenContents(onRouteButtonClicked: (Routes) -> Unit) {
    Surface(modifier = Modifier
        .statusBarsPadding()
        .fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(Modifier.padding(7.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.radio_button_checked_24px),
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                text = stringResource(R.string.IntroTextTop),
                fontWeight = FontWeight(500),
                fontSize = 25.sp,
            )
            /* add some fluff */
            /*
            github
            licences/ licence
             */
            Spacer(Modifier.padding(vertical = 330.dp))
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
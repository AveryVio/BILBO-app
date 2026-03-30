package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.storage.InstrumentDBRow
import com.averyvi.bilbo.storage.UserDao
import com.averyvi.bilbo.storage.getAllInstruments
import com.averyvi.bilbo.ui.fragments.DeviceList
import com.averyvi.bilbo.ui.fragments.InstrumentShelfItem
import com.averyvi.bilbo.ui.fragments.IntroAppTitle
import com.averyvi.bilbo.ui.fragments.IntroTutorial
import com.averyvi.bilbo.ui.fragments.PitchDiffView
import com.averyvi.bilbo.storage.deleteAllInstruments
import com.averyvi.bilbo.storage.deleteLastInstrument
import com.averyvi.bilbo.ui.fragments.InstrumentViewModel
import com.averyvi.bilbo.ui.fragments.NewInstrumentSelector
import com.averyvi.bilbo.ui.fragments.TuningViewModel
import kotlin.concurrent.thread

@Composable
fun NowPlayingScreen(
    tuningViewModel: TuningViewModel,
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
                PitchDiffView(tuningViewModel.pitch.collectAsState().value)
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
    dbDao: UserDao,
){
    val instruments: SnapshotStateList<InstrumentDBRow> = getAllInstruments(dbDao)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
    ) {
        Row() {
            Button(
                onClick = {
                    thread {
                        dbDao.insertAll(
                            instrument = InstrumentDBRow(
                                instrumentName = "Piano",
                                instrumentIcon = R.drawable.radio_button_checked_24px,
                                refFreq = 440,
                                positionInOctive = 9,
                                refOctive = 4,
                            )
                        )
                    }
                }
            ){
                Text("testAdd") // toto remove
            }
            Button(
                onClick = {
                    thread {
                        deleteAllInstruments(dbDao)
                    }
                }
            ){
                Text("testRemoveAll") // toto remove
            }
            Button(
                onClick = {
                    thread {
                        deleteLastInstrument(dbDao)
                    }
                }
            ){
                Text("testRemoveLast") // toto remove
            }
        }
        if(instruments.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(top = 6.dp),
                columns = GridCells.Adaptive(75.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(instruments.size) {
                    InstrumentShelfItem(
                        InstrumentImageResource = instruments[it].instrumentIcon,
                        name = instruments[it].instrumentName,
                    )
                }
            }
        } else {
            Text("fjdkl") // todo add string
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInstrumentScreen(
    viewModel: InstrumentViewModel,
    onRouteButtonClicked: (Routes) -> Unit,
    dbDao: UserDao,
){
    val selectedName by viewModel.name.collectAsState()
    val selectedFreq by viewModel.freq.collectAsState()
    val selectedNote by viewModel.note.collectAsState()
    val selectedOctive by viewModel.octive.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(R.string.NewInstrumentScreen),
                fontSize = 40.sp,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                modifier = Modifier
            )
        }
        NewInstrumentSelector(
            selectedName = selectedName,
            onNameChange = { it: String ->
                if (it.length < 50) {
                    viewModel.updateName(it)
                }
            },
            selectedFreqString = selectedFreq,
            onFreqChange = {
                if (it == "") {
                    viewModel.updateFreq(it)
                } else {
                    if (it.length < 30) {
                        viewModel.updateFreq(it.filter { numb -> numb.isDigit() })
                    }
                }
            },
            selectedNote = selectedNote,
            onSelectedNoteChange = { viewModel.updateNote(it) } ,
            selectedOctive = selectedOctive,
            onSelectedOctiveChange = { viewModel.updateOctive(it) }
        )
    }
}

@Composable
fun IntroScreen(
    onRouteButtonClicked: (Routes) -> Unit
) {
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

//About screen

/*
val a = InstrumentStyling(instrumentName = "Piano", instrumentIcon = R.drawable.androidicon)
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


}*/
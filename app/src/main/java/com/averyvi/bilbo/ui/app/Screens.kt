package com.averyvi.bilbo.ui.app

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes
import com.averyvi.bilbo.definitions.MusicalNote
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.storage.InstrumentDBRow
import com.averyvi.bilbo.storage.UserDao
import com.averyvi.bilbo.storage.getAllInstruments
import com.averyvi.bilbo.ui.fragments.DeviceList
import com.averyvi.bilbo.ui.fragments.InstrumentShelfItem
import com.averyvi.bilbo.ui.fragments.IntroAppTitle
import com.averyvi.bilbo.ui.fragments.IntroTutorial
import com.averyvi.bilbo.ui.fragments.NewInstrumentDropdown
import com.averyvi.bilbo.ui.fragments.PitchDiffView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.averyvi.bilbo.storage.deleteAllInstruments
import com.averyvi.bilbo.storage.deleteLastInstrument
import com.averyvi.bilbo.ui.fragments.NewInstrumentSelector
import com.averyvi.bilbo.ui.fragments.NewInstrumentTextInput
import kotlin.concurrent.thread

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
    onRouteButtonClicked: (Routes) -> Unit,
    dbDao: UserDao,
){
    var selectedName: String by remember { mutableStateOf("") }
    var selectedFreq: Int by remember { mutableStateOf(440) }
    var selectedFreqString: String by remember { mutableStateOf("") }
    var selectedNote: MusicalNote by remember { mutableStateOf(MusicalNote.A) }
    var selectedOctive: Int by remember { mutableStateOf(4) }

    var noteIsExpanded by remember { mutableStateOf(false) }
    var octiveIsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).padding(15.dp),
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
                    selectedName = it
                }
            },
            selectedFreqString = selectedFreqString,
            onFreqChange = {
                if (it == "") {
                    selectedFreqString = ""
                } else {
                    if (it.length < 30) {
                        selectedFreqString = it.filter { numb -> numb.isDigit() }
                    }
                }
            },
            selectedNote = selectedNote,
            onSelectedNoteChange = { selectedNote = it },
            selectedOctive = selectedOctive,
            onSelectedOctiveChange = {selectedOctive = it}
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    // make variables into values
                    if (selectedFreqString.length == 0) {
                        selectedFreq = 0
                    } else if (selectedFreqString.length < 10) {
                        selectedFreq = selectedFreqString.takeLast(10).toInt()
                    }
                    //insert into db
                    thread {
                        dbDao.insertAll(
                            instrument = InstrumentDBRow(
                                instrumentName = selectedName,
                                instrumentIcon = R.drawable.radio_button_checked_24px, //todo add the possibility of adding an icon
                                refFreq = selectedFreq,
                                positionInOctive = selectedNote.ordinal,
                                refOctive = selectedOctive
                            )
                        )
                    }
                    //navigate
                    onRouteButtonClicked(Routes.InstrumentSelect)
                },
                modifier = Modifier.width(IntrinsicSize.Max).height(IntrinsicSize.Max),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_24dp_000000_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    modifier = Modifier.height(50.dp).width(50.dp)
                )
                Text(
                    text = stringResource(R.string.AddButton),
                    fontSize = 30.sp,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                )
            }
        }
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